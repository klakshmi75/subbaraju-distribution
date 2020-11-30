package com.lakshmi.poc.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lakshmi.poc.model.TripDetail;
import com.lakshmi.poc.model.TripsRequest;
import com.lakshmi.poc.pojo.SheetDetails;
import com.lakshmi.poc.pojo.WorkbookDetails;
import com.lakshmi.poc.utils.ExcelFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Slf4j
public class TripsController {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/trips", method = RequestMethod.GET, produces = "application/json")
    public List<Map<String, Object>> firstPage(@Valid TripsRequest tripsRequest) {
        log.info("TripsRequest\n {}", tripsRequest);
        return getDummyList(tripsRequest);
    }

    private List<Map<String, Object>> getDummyList(TripsRequest tripsRequest) {
        String query;
        Map<String, Object> queryParams = Maps.newHashMap();

        WorkbookDetails workbookDetails = ExcelFormatUtils.getBillingDetailsFormat();
        SheetDetails abstractDetails = workbookDetails.getSheetDetailsList().get(0);

        if(tripsRequest.isDateRange()) {
            query = abstractDetails.getQueryByDateRange();
            queryParams.put("depo", "Chagallu");
            queryParams.put("startDate", tripsRequest.getStartDate());
            queryParams.put("endDate", tripsRequest.getEndDate());
        } else {
            query = abstractDetails.getQueryByDate();
            queryParams.put("depo", "Chagallu");
            queryParams.put("date", tripsRequest.getDate());
        }

        List<Map<String, Object>> testRresults = jdbcTemplate.queryForList(query, queryParams);
        log.info(">>>>>>>>>>>>>>>>>>" + testRresults.toString());
        return testRresults;
    }
}
