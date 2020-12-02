package com.lakshmi.poc.utils;

import com.lakshmi.poc.enums.ColumnDataType;
import com.lakshmi.poc.enums.Depo;
import com.lakshmi.poc.pojo.ColumnDetails;
import com.lakshmi.poc.pojo.SheetDetails;
import com.lakshmi.poc.pojo.WorkbookDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelExportUtils {
    private static final String DISTRICT_HEADER = "District Name - $DISTRICT";
    private static final String DEPO_HEADER = "Depo Name - $DISTRICT - $DEPO_CODE ($DEPO_NAME)";
    private static final String SERIAL_NO_HEADER = "S.No.";

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
            for(Map<String, Object> dataRow: data) {
                row = sheet.createRow(rowIndex++);
                addDataRow(row, dataRow, columnDetailsList);
            }

            // Add Total Row
            row = sheet.createRow(rowIndex++);
            addTotalRow(row, columnDetailsList, sheetDetails.isAddDepoHeader());

            // Set auto size for all the columns
            for(int i =0; i <= columnDetailsList.size() ; i++) {
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
        // Merge first and second cells
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

        // Add Depo Name in 3rd and 4th cell of first row
        cell = row.createCell(2);
        cell.setCellValue(getDepoHeader(depo));
        // Merge third and fourth cells
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
    }

    private static void addHeaderRow(Row row, List<ColumnDetails> columnDetailsList) {
        Cell cell;
        int cellIndex = 0;
        // Add header "S.No."
        cell = row.createCell(cellIndex++);
        cell.setCellValue(SERIAL_NO_HEADER);

        // Iterate each column detail and add header cell
        for(ColumnDetails columnDetail: columnDetailsList) {
            cell = row.createCell(cellIndex++);
            cell.setCellValue(columnDetail.getHeader());
        }
    }

    private static void addDataRow(Row row, Map<String, Object> dataRow, List<ColumnDetails> columnDetailsList) {
        Cell cell;
        int cellIndex = 0;
        // Add Serial Number
        cell = row.createCell(cellIndex++);
        cell.setCellValue(row.getRowNum() - 1);

        // Iterate each column detail and add data cell
        for(ColumnDetails columnDetail: columnDetailsList) {
            switch (columnDetail.getType()) {
                case STRING:
                    cell = row.createCell(cellIndex++);
                    cell.setCellValue((String)dataRow.get(columnDetail.getHeader()));

                    break;
                case INT:
                    cell = row.createCell(cellIndex++);
                    cell.setCellValue(((BigDecimal)dataRow.get(columnDetail.getHeader())).intValue());
                    break;
                case DATE:
                    cell = row.createCell(cellIndex++);
                    cell.setCellValue((Date)dataRow.get(columnDetail.getHeader()));
                    break;
            }
            CellStyle cellStyle = getCellStyle(row, columnDetail.getType());
            cell.setCellStyle(cellStyle);
        }
    }

    private static void addTotalRow(Row row, List<ColumnDetails> columnDetailsList, boolean depoNameHeaderExists) {
        Cell cell;
        // Add Total label under Date column (2nd column)
        cell = row.createCell(1);
        cell.setCellValue("Total");
        cell.setCellStyle(getCellStyle(row, ColumnDataType.STRING));

        // Add sum formula for number type columns
        char headerChar = 'A'; // skip S.No
        int cellIndex = 0;
        for(ColumnDetails columnDetails: columnDetailsList) {
            cellIndex++;
            headerChar++;
            if(columnDetails.isAddTotal()) {
                cell = row.createCell(cellIndex);
                // Get cell numbers for formula Ex: "SUM(A1:A10)"
                int fromRowNum = depoNameHeaderExists ? 3 : 2;
                int toRowNum = row.getRowNum();
                String fromCell = String.valueOf(headerChar) + fromRowNum;
                String toCell = String.valueOf(headerChar) + toRowNum;
                cell.setCellFormula("SUM(" +  fromCell + ":" + toCell + ")");
                cell.setCellStyle(getCellStyle(row, ColumnDataType.INT));
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

        switch (dataType) {
            case STRING:
                cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
                break;
            case INT:
                cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
                break;
            case DATE:
                cellStyle = workbook.createCellStyle();
                cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mmm/yy"));
                cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
                break;
        }

        return cellStyle;
    }
}
