package com.lakshmi.poc.controller;

import com.google.common.collect.Lists;
import com.lakshmi.poc.model.TripDetail;
import com.lakshmi.poc.model.TripsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/trips", method = RequestMethod.GET, produces = "application/json")
    public List<TripDetail> firstPage(@Valid TripsRequest tripsRequest) {
        log.info("&&&&&*******************depo name {}", tripsRequest.getDepo());
        return getDummyList();
    }

    private List<TripDetail> getDummyList() {
        List<TripDetail> list = Lists.newArrayList();
        List<Map<String, Object>> testRresults = jdbcTemplate.queryForList("select * from test");
        log.info(">>>>>>>>>>>>>>>>>>" + testRresults.toString());
        return list;
    }
}
