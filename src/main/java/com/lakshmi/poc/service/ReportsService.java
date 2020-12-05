package com.lakshmi.poc.service;

import com.google.common.collect.Maps;
import com.lakshmi.poc.dao.ReportsDao;
import com.lakshmi.poc.model.SigmaBillingRequest;
import com.lakshmi.poc.pojo.WorkbookDetails;
import com.lakshmi.poc.utils.DateUtils;
import com.lakshmi.poc.utils.ExcelHelper;
import com.lakshmi.poc.utils.ExcelFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ReportsDao reportsDao;

    public void generateSigmaBillingReport(SigmaBillingRequest sigmaBillingRequest, OutputStream os) throws IOException {
        log.info("Generating sigma billing report...");

        WorkbookDetails workbookDetails = ExcelFormatUtils.getSigmaBillingDetailsFormat();
        // ***** keys in data map should be same as defined in workbook details json
        Map<Integer, List<Map<String, Object>>> dataBySheetPosition = Maps.newHashMap();
        dataBySheetPosition.put(1, reportsDao.getAbstractData(sigmaBillingRequest));
        dataBySheetPosition.put(2, reportsDao.getOutletWiseData(sigmaBillingRequest));
        dataBySheetPosition.put(3, reportsDao.getSlabWiseData(sigmaBillingRequest));
        ExcelHelper.generateExcelReport(workbookDetails, dataBySheetPosition, os, sigmaBillingRequest.getDepo());
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
