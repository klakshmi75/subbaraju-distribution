package com.lakshmi.poc.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lakshmi.poc.model.SigmaBillingRequest;
import com.lakshmi.poc.pojo.WorkbookDetails;
import com.lakshmi.poc.utils.DateUtils;
import com.lakshmi.poc.utils.ExcelExportUtils;
import com.lakshmi.poc.utils.ExcelFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ReportsService {
    private static final String SIGMA_BILLING_FILE_NAME_DATE_RANGE = "APSBCL-$DEPO_ABBR-TRANSPORT CASES DETAILS $START_DATE To $END_DATE.xlsx";
    private static final String SIGMA_BILLING_FILE_NAME_DATE = "APSBCL-$DEPO_ABBR-TRANSPORT CASES DETAILS $DATE.xlsx";
    private static final String ABSTRACT_QUERY_BY_DATE = "SELECT date AS 'Date', SUM(num_cases_imfl + num_cases_beer) AS 'Total Cases', SUM(form_3) AS 'Form 3 (In Amount)' FROM trip_details td JOIN outlet_master om ON td.outlet_code = om.outlet_code AND om.depo_name = :depoName GROUP BY date  HAVING date = :date";
    private static final String ABSTRACT_QUERY_BY_DATE_RANGE = "SELECT date AS 'Date', SUM(num_cases_imfl + num_cases_beer) AS 'Total Cases', SUM(form_3) AS 'Form 3 (In Amount)' FROM trip_details td JOIN outlet_master om ON td.outlet_code = om.outlet_code AND om.depo_name = :depoName GROUP BY date  HAVING date BETWEEN :startDate AND :endDate";
    private static final String OUTLET_DETAILS_QUERY_BY_DATE = "SELECT date AS 'Date', vehicle_number AS 'Vehicle Number', outlet_address AS 'Outlet Address', Station AS 'Station', km_from_depo AS 'KM From depo', om.outlet_code AS 'Outlet Code', num_cases_imfl AS 'IMFL', num_cases_beer AS 'BEER', (num_cases_imfl + num_cases_beer) AS 'Total Cases', form_3 AS 'Form 3 (In Amount)' FROM  trip_details td JOIN outlet_master om ON td.outlet_code = om.outlet_code AND om.depo_name = :depoName WHERE date = :date";
    private static final String OUTLET_DETAILS_QUERY_BY_DATE_RANGE = "SELECT date AS 'Date', vehicle_number AS 'Vehicle Number', outlet_address AS 'Outlet Address', Station AS 'Station', km_from_depo AS 'KM From depo', om.outlet_code AS 'Outlet Code', num_cases_imfl AS 'IMFL', num_cases_beer AS 'BEER', (num_cases_imfl + num_cases_beer) AS 'Total Cases', form_3 AS 'Form 3 (In Amount)' FROM  trip_details td  JOIN outlet_master om ON td.outlet_code = om.outlet_code AND om.depo_name = :depoName WHERE date BETWEEN :startDate AND endDate";
    private static final String SLAB_WISE_DETAILS_QUERY_BY_DATE = "SELECT date AS 'date', SUM(num_cases_imfl+num_cases_beer) AS 'cases', sm.slab_code AS 'slab_code' FROM trip_details td JOIN outlet_master om ON td.outlet_code = om.outlet_code AND om.depo_name = :depoName JOIN slab_master sm ON km_from_depo BETWEEN range_min AND sm.range_max GROUP BY date, sm.slab_code HAVING date = :date";
    private static final String SLAB_WISE_DETAILS_QUERY_BY_DATE_RANGE = "SELECT date AS 'date', SUM(num_cases_imfl+num_cases_beer) AS 'cases', sm.slab_code AS 'slab_code' FROM trip_details td JOIN outlet_master om ON td.outlet_code = om.outlet_code AND om.depo_name = :depoName JOIN slab_master sm ON km_from_depo BETWEEN range_min AND sm.range_max GROUP BY date, sm.slab_code HAVING date BETWEEN :startDate AND :endDate";
    private static final String All_SLABS_QUERY = "select slab_code AS 'slab_code', concat(slab_code, ' Cases ', range_min, '-', range_max, 'Km')  AS 'slab_detail' from slab_master";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void generateSigmaBillingReport(SigmaBillingRequest sigmaBillingRequest, OutputStream os) throws IOException {
        String query;
        Map<String, Object> queryParams = Maps.newHashMap();
        log.info("Generating sigma billing report...");

        WorkbookDetails workbookDetails = ExcelFormatUtils.getSigmaBillingDetailsFormat();
        // ***** keys in data map should be same as defined in workbook details json
        Map<Integer, List<Map<String, Object>>> dataBySheetPosition = Maps.newHashMap();
        dataBySheetPosition.put(1, getAbstractData(sigmaBillingRequest));
        dataBySheetPosition.put(2, getOutletWiseData(sigmaBillingRequest));
        dataBySheetPosition.put(3, getSlabWiseData(sigmaBillingRequest));
        ExcelExportUtils.generateExcelReport(workbookDetails, dataBySheetPosition, os, sigmaBillingRequest.getDepo());
    }

    private List<Map<String, Object>> getAbstractData(SigmaBillingRequest sigmaBillingRequest) {
        String query;
        if(sigmaBillingRequest.isDateRange()) {
            query = ABSTRACT_QUERY_BY_DATE_RANGE;
        } else {
            query = ABSTRACT_QUERY_BY_DATE;
        }
        List<Map<String, Object>> rawData = namedParameterJdbcTemplate.queryForList(query, getQueryParamsForSigmaBilling(sigmaBillingRequest));

        return rawData;
    }

    private List<Map<String, Object>> getOutletWiseData(SigmaBillingRequest sigmaBillingRequest) {
        String query;
        if(sigmaBillingRequest.isDateRange()) {
            query = OUTLET_DETAILS_QUERY_BY_DATE_RANGE;
        } else {
            query = OUTLET_DETAILS_QUERY_BY_DATE;
        }
        List<Map<String, Object>> rawData = namedParameterJdbcTemplate.queryForList(query, getQueryParamsForSigmaBilling(sigmaBillingRequest));

        return rawData;
    }

    private List<Map<String, Object>> getSlabWiseData(SigmaBillingRequest sigmaBillingRequest) {
        String query;
        if(sigmaBillingRequest.isDateRange()) {
            query = SLAB_WISE_DETAILS_QUERY_BY_DATE_RANGE;
        } else {
            query = SLAB_WISE_DETAILS_QUERY_BY_DATE;
        }
        List<Map<String, Object>> rawData = namedParameterJdbcTemplate.queryForList(query, getQueryParamsForSigmaBilling(sigmaBillingRequest));

        return processSlabWiseData(rawData);
    }

    private List<Map<String, Object>> processSlabWiseData(List<Map<String, Object>> rawData) {
        Map<Date, Map<String, Integer>> dateWiseMap = Maps.newHashMap();
        Map<String, Integer> slabWiseMap;
        // Collect cases into map by date and slab code
        for(Map<String, Object> dataRow: rawData) {
            Date date = (Date)dataRow.get("date");
            if(dateWiseMap.containsKey(date)) {
                slabWiseMap = dateWiseMap.get(date);
            } else {
                slabWiseMap = Maps.newHashMap();
                dateWiseMap.put(date, slabWiseMap);
            }
            int cases = ((BigDecimal)dataRow.get("cases")).intValue();
            String slabCode = (String)dataRow.get("slab_code");
            slabWiseMap.put(slabCode, cases);
        }

        // Prepare final data map with actual headers in json
        List<Map<String, Object>> finalData = Lists.newArrayList();
        Map<String, Object> finalDataRow;
        Map<String, String> slabCodeDetailMap = getAllSlabsMap(); // To get all slabs irrespective of data
        for(Map.Entry<Date, Map<String, Integer>> dateWiseEntry: dateWiseMap.entrySet()) {
            finalDataRow = Maps.newHashMap();
            finalDataRow.put("Date", dateWiseEntry.getKey());
            Map<String, Integer> slabWiseCases = dateWiseEntry.getValue();
            int totalCases = 0;
            for(String slabCode: slabCodeDetailMap.keySet()) {
                Integer cases = slabWiseCases.get(slabCode);
                totalCases += cases != null ? cases : 0;
                finalDataRow.put(slabCodeDetailMap.get(slabCode), cases != null ? cases : 0); // Expanded slab detail as key
            }
            finalDataRow.put("Total Cases", totalCases);
            finalData.add(finalDataRow);
        }

        return finalData;
    }

    private Map<String, String> getAllSlabsMap() {
        Map<String, String> slabCodeDetailMap = Maps.newHashMap();
        List<Map<String, Object>> allSlabsData = jdbcTemplate.queryForList(All_SLABS_QUERY);
        for(Map<String, Object> slabDetailMap : allSlabsData) {
            String slabCode = (String)slabDetailMap.get("slab_code");
            String slabDetail = (String)slabDetailMap.get("slab_detail");
            slabCodeDetailMap.put(slabCode, slabDetail);
        }

        return slabCodeDetailMap;
    }

    private Map<String, Object> getQueryParamsForSigmaBilling(SigmaBillingRequest sigmaBillingRequest) {
        Map<String, Object> queryParams = Maps.newHashMap();
        if (sigmaBillingRequest.isDateRange()) {
            queryParams.put("depoName", sigmaBillingRequest.getDepo().getDepoName());
            queryParams.put("startDate", sigmaBillingRequest.getStartDate());
            queryParams.put("endDate", sigmaBillingRequest.getEndDate());
        } else {
            queryParams.put("depoName", sigmaBillingRequest.getDepo().getDepoName());
            queryParams.put("date", sigmaBillingRequest.getDate());
        }

        return queryParams;
    }

    public String getSigmaBillingFileName(SigmaBillingRequest sigmaBillingRequest) {
        String fileName;
        // Don't forget to assign to same variable after replacing place holders
        if(sigmaBillingRequest.isDateRange()) {
            fileName = SIGMA_BILLING_FILE_NAME_DATE_RANGE;
            fileName = fileName.replace("$START_DATE", DateUtils.formatDateForFileName(sigmaBillingRequest.getStartDate()));
            fileName = fileName.replace("$END_DATE", DateUtils.formatDateForFileName(sigmaBillingRequest.getEndDate()));
        } else {
            fileName = SIGMA_BILLING_FILE_NAME_DATE;
            fileName = fileName.replace("$DATE", DateUtils.formatDateForFileName(sigmaBillingRequest.getDate()));
        }
        fileName = fileName.replace("$DEPO_ABBR", sigmaBillingRequest.getDepo().getDepoAbbr());
        return fileName;
    }
}
