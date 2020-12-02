package com.lakshmi.poc.service;

import com.google.common.collect.Maps;
import com.lakshmi.poc.model.SigmaBillingRequest;
import com.lakshmi.poc.pojo.WorkbookDetails;
import com.lakshmi.poc.utils.DateUtils;
import com.lakshmi.poc.utils.ExcelExportUtils;
import com.lakshmi.poc.utils.ExcelFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ReportsService {
    private static final String SIGMA_BILLING_FILE_NAME_DATE_RANGE = "APSBCL-$DEPO_ABBR-TRANSPORT CASES DETAILS $START_DATE To $END_DATE.xlsx";
    private static final String SIGMA_BILLING_FILE_NAME_DATE = "APSBCL-$DEPO_ABBR-TRANSPORT CASES DETAILS $DATE.xlsx";
    private static final String SIGMA_BILLING_QUERY_BY_DATE = "SELECT date AS 'Date', SUM(num_cases_imfl + num_cases_beer) AS 'Total Cases', SUM(form_3) AS 'Form 3 (In Amount)' FROM trip_details td LEFT JOIN outlet_master om ON td.outlet_code = om.outlet_code AND om.depo_name = :depo GROUP BY date  HAVING date = :date";
    private static final String SIGMA_BILLING_QUERY_BY_DATE_RANGE = "SELECT date AS 'Date', SUM(num_cases_imfl + num_cases_beer) AS 'Total Cases', SUM(form_3) AS 'Form 3 (In Amount)' FROM trip_details td LEFT JOIN outlet_master om ON td.outlet_code = om.outlet_code AND om.depo_name = :depo GROUP BY date  HAVING date between :startDate and :endDate";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void generateSigmaBillingReport(SigmaBillingRequest sigmaBillingRequest, OutputStream os) throws IOException {
        String query;
        Map<String, Object> queryParams = Maps.newHashMap();
        log.info("Generating sigma billing report...");

        WorkbookDetails workbookDetails = ExcelFormatUtils.getSigmaBillingDetailsFormat();
        Map<Integer, List<Map<String, Object>>> dataBySheetPosition = Maps.newHashMap();
        dataBySheetPosition.put(1, getAbstractData(sigmaBillingRequest));
//        dataBySheetPosition.put(2, getOutletWiseData(sigmaBillingRequest));
//        dataBySheetPosition.put(3, getSlabWiseData(sigmaBillingRequest));
        ExcelExportUtils.generateExcelReport(workbookDetails, dataBySheetPosition, os, sigmaBillingRequest.getDepo());
    }

    private List<Map<String, Object>> getAbstractData(SigmaBillingRequest sigmaBillingRequest) {
        String query;
        if(sigmaBillingRequest.isDateRange()) {
            query = SIGMA_BILLING_QUERY_BY_DATE_RANGE;
        } else {
            query = SIGMA_BILLING_QUERY_BY_DATE;
        }
        List<Map<String, Object>> rawData = jdbcTemplate.queryForList(query, getQueryParamsForSigmaBilling(sigmaBillingRequest));

        return rawData;
    }

    private List<Map<String, Object>> getOutletWiseData(SigmaBillingRequest sigmaBillingRequest) {
        String query;
        if(sigmaBillingRequest.isDateRange()) {
            query = SIGMA_BILLING_QUERY_BY_DATE_RANGE;
        } else {
            query = SIGMA_BILLING_QUERY_BY_DATE;
        }
        List<Map<String, Object>> rawData = jdbcTemplate.queryForList(query, getQueryParamsForSigmaBilling(sigmaBillingRequest));

        return rawData;
    }

    private List<Map<String, Object>> getSlabWiseData(SigmaBillingRequest sigmaBillingRequest) {
        String query;
        if(sigmaBillingRequest.isDateRange()) {
            query = SIGMA_BILLING_QUERY_BY_DATE_RANGE;
        } else {
            query = SIGMA_BILLING_QUERY_BY_DATE;
        }
        List<Map<String, Object>> rawData = jdbcTemplate.queryForList(query, getQueryParamsForSigmaBilling(sigmaBillingRequest));

        return rawData;
    }

    private Map<String, Object> getQueryParamsForSigmaBilling(SigmaBillingRequest sigmaBillingRequest) {
        Map<String, Object> queryParams = Maps.newHashMap();
        if (sigmaBillingRequest.isDateRange()) {
            queryParams.put("depo", "Chagallu");
            queryParams.put("startDate", sigmaBillingRequest.getStartDate());
            queryParams.put("endDate", sigmaBillingRequest.getEndDate());
        } else {
            queryParams.put("depo", "Chagallu");
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
