package com.lakshmi.poc.utils;

import com.google.common.collect.Lists;
import com.lakshmi.poc.enums.ColumnDataType;
import com.lakshmi.poc.enums.Depo;
import com.lakshmi.poc.exception.CustomException;
import com.lakshmi.poc.model.TripDetail;
import com.lakshmi.poc.pojo.ColumnDetails;
import com.lakshmi.poc.pojo.SheetDetails;
import com.lakshmi.poc.pojo.WorkbookDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelHelper {
    private static final String DISTRICT_HEADER = "District Name - $DISTRICT";
    private static final String DEPO_HEADER = "Depo Name - $DISTRICT - $DEPO_CODE ($DEPO_NAME)";
    private static final String SERIAL_NO_HEADER = "S.No.";

    // This method assumes the data will be present in the predefined order along with below headers.
    // Date	 Outlet Code	 Vehicle Number	 IMFL	 BEER	 Form-3
    public static List<TripDetail> excelToTripDetails(MultipartFile file) {
        List<TripDetail> tripDetails = Lists.newArrayList();
        String fileName = file.getOriginalFilename();
        LocalDate date = null;
        String outletCode = null;
        String vehicleNumber = null;
        Integer imfl = null;
        Integer beer = null;
        Integer form3 = null;

        try (InputStream is = file.getInputStream()){
            log.info("Reading excel file : {} for trip details", fileName);
//            InputStream is = file.getInputStream();

            //Create Workbook instance for xlsx/xls file input stream
            Workbook workbook = null;
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(is);
            }

            Sheet sheet = workbook.getSheetAt(0);
            log.info("Processing only the first sheet {}", sheet.getSheetName());
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if(row.getRowNum() == 0) {
                    continue; // Skip headers
                }
                Cell firstCell = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
                if(firstCell == null) {
                    // End of excel rows
                    break;
                }
                String dateStr = firstCell.getStringCellValue();
                date = DateUtils.getLocalDateFromString(dateStr);
                outletCode = row.getCell(1).getStringCellValue();
//                outletCode = String.valueOf(row.getCell(1).getNumericCellValue()); // outlet code column is created a s numeric
                vehicleNumber = row.getCell(2).getStringCellValue();
                imfl = (int) row.getCell(3).getNumericCellValue();
                beer = (int) row.getCell(4).getNumericCellValue();
                form3 = (int) row.getCell(5).getNumericCellValue();

                TripDetail tripDetail = new TripDetail(date, outletCode, vehicleNumber, imfl, beer, form3);
                tripDetails.add(tripDetail);
            }
        } catch (Exception e) {
            throw new CustomException("Exception occurred while uploading file.", e);
        }

        return tripDetails;
    }

    public static void generateExcelReport(WorkbookDetails workbookDetails, Map<Integer, List<Map<String, Object>>> dataBySheetPosition, OutputStream os, Depo depo) throws IOException {
        List<Map<String, Object>> data;
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet;
        Row row;
        Cell cell;
        int rowIndex;
        int cellIndex;
        for (SheetDetails sheetDetails : workbookDetails.getSheetDetailsList()) {
            data = dataBySheetPosition.get(sheetDetails.getSheetPosition());
            sheet = workbook.createSheet(sheetDetails.getSheetName());
            List<ColumnDetails> columnDetailsList = sheetDetails.getColumnsDetailsList();
            rowIndex = 0;


            // Add Depo header row if required
            if (sheetDetails.isAddDepoHeader()) {
                row = sheet.createRow(rowIndex++);
                addDepoHeaderRow(sheet, row, depo);
            }

            // Add header row
            row = sheet.createRow(rowIndex++);
            addHeaderRow(row, columnDetailsList);

            // Add data rows
            for (Map<String, Object> dataRow : data) {
                row = sheet.createRow(rowIndex++);
                addDataRow(row, dataRow, columnDetailsList, sheetDetails.isAddDepoHeader());
            }

            // Add Total Row
            row = sheet.createRow(rowIndex++);
            addTotalRow(row, columnDetailsList, sheetDetails.isAddDepoHeader());

            // Set auto size for all the columns
            for (int i = 0; i <= columnDetailsList.size(); i++) {
                sheet.autoSizeColumn(i);
            }
        }
        workbook.write(os);
        os.flush();
    }

    private static void addDepoHeaderRow(Sheet sheet, Row row, Depo depo) {
        Cell cell;
        // Add District Name header in first 2 cells of first row
        cell = row.createCell(0);
        cell.setCellValue(getDistrictHeader(depo));
        cell.setCellStyle(getHeaderStyle(row, false));
        // Merge first and second cells
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

        // Add Depo Name in 3rd and 4th cell of first row
        cell = row.createCell(2);
        cell.setCellValue(getDepoHeader(depo));
        cell.setCellStyle(getHeaderStyle(row, false));
        // Merge third and fourth cells
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
    }

    private static void addHeaderRow(Row row, List<ColumnDetails> columnDetailsList) {
        Cell cell;
        int cellIndex = 0;
        // Add header "S.No."
        cell = row.createCell(cellIndex++);
        cell.setCellValue(SERIAL_NO_HEADER);
        cell.setCellStyle(getHeaderStyle(row, false));

        // Iterate each column detail and add header cell
        for (ColumnDetails columnDetail : columnDetailsList) {
            cell = row.createCell(cellIndex++);
            cell.setCellValue(columnDetail.getHeader());
            cell.setCellStyle(getHeaderStyle(row, false));
        }
    }

    private static void addDataRow(Row row, Map<String, Object> dataRow, List<ColumnDetails> columnDetailsList, boolean depoNameHeaderExists) {
        Cell cell;
        int cellIndex = 0;
        log.info("Adding data row : {}", row.getRowNum());
        log.info("Data : {}", dataRow);
        // Add Serial Number
        cell = row.createCell(cellIndex++);
        // If depo header row exists serial number will be 1 less than row number
        cell.setCellValue(depoNameHeaderExists ? row.getRowNum() - 1 : row.getRowNum());

        // Iterate each column detail and add data cell
        for (ColumnDetails columnDetail : columnDetailsList) {
            cell = row.createCell(cellIndex++);
            log.debug("Adding data cell. Header : {}, Type : {}", columnDetail.getHeader(), columnDetail.getType());
            switch (columnDetail.getType()) {
                case STRING:
                    cell.setCellValue((String) dataRow.get(columnDetail.getHeader()));
                    break;
                case INT:
                    cell.setCellValue((Integer) dataRow.get(columnDetail.getHeader()));
                    break;
                case LONG:
                    cell.setCellValue((Long) dataRow.get(columnDetail.getHeader()));
                    break;
                case DOUBLE:
                    cell.setCellValue((Double) dataRow.get(columnDetail.getHeader()));
                    break;
                case BIG_DECIMAL:
                    cell.setCellValue(((BigDecimal) dataRow.get(columnDetail.getHeader())).intValue());
                    break;
                case DATE:
                    cell.setCellValue((Date) dataRow.get(columnDetail.getHeader()));
                    break;
            }
            CellStyle cellStyle = getCellStyle(row, columnDetail.getType());
            cell.setCellStyle(cellStyle);
        }
    }

    private static void addTotalRow(Row row, List<ColumnDetails> columnDetailsList, boolean depoNameHeaderExists) {
        Cell cell;
        // Create empty cell under S.No. (to be able to apply background color)
        cell = row.createCell(0);
        cell.setCellValue("");
        cell.setCellStyle(getHeaderStyle(row, false));

        // Add sum formula for number type columns
        char headerChar = 'A'; // skip S.No
        int cellIndex = 0;
        for (ColumnDetails columnDetails : columnDetailsList) {
            cellIndex++;
            headerChar++;
            cell = row.createCell(cellIndex);
            if (columnDetails.isAddTotal()) {
                // Get cell numbers for formula Ex: "SUM(A1:A10)"
                int fromRowNum = depoNameHeaderExists ? 3 : 2;
                int toRowNum = row.getRowNum();
                String fromCell = String.valueOf(headerChar) + fromRowNum;
                String toCell = String.valueOf(headerChar) + toRowNum;
                cell.setCellFormula("SUM(" + fromCell + ":" + toCell + ")");
                cell.setCellStyle(getHeaderStyle(row, true));
            } else {
                if (cellIndex == 1) { // Date column
                    cell.setCellValue("Total");
                } else {
                    cell.setCellValue(""); // Empty cell to fill background color
                }
                cell.setCellStyle(getHeaderStyle(row, false));
            }

        }
    }

    private static String getDistrictHeader(Depo depo) {

        return DISTRICT_HEADER.replace("$DISTRICT", depo.getDistrict());
    }

    private static String getDepoHeader(Depo depo) {
        String header = DEPO_HEADER.replace("$DISTRICT", depo.getDistrict());
        header = header.replace("$DEPO_CODE", String.valueOf(depo.getDepoCode()));
        header = header.replace("$DEPO_NAME", depo.getDepoName());

        return header;
    }

    private static CellStyle getCellStyle(Row row, ColumnDataType dataType) {
        CellStyle cellStyle = null;
        Workbook workbook = row.getSheet().getWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle = workbook.createCellStyle();
        switch (dataType) {
            case STRING:
                cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
                break;
            case INT:
                cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
                break;
            case DATE:
                cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mmm/yy"));
                cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
                break;
        }

        return cellStyle;
    }

    private static CellStyle getHeaderStyle(Row row, boolean isNumberType) {
        CellStyle cellStyle;
        if (isNumberType) {
            cellStyle = getCellStyle(row, ColumnDataType.INT);
        } else {
            cellStyle = getCellStyle(row, ColumnDataType.STRING);
        }
        // Header font
        Font headerFont = row.getSheet().getWorkbook().createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        cellStyle.setFont(headerFont);
        cellStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return cellStyle;
    }
}
