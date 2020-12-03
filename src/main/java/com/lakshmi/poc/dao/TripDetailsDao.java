package com.lakshmi.poc.dao;

import com.google.common.collect.Lists;
import com.lakshmi.poc.exception.InvalidInputException;
import com.lakshmi.poc.model.TripDetail;
import com.lakshmi.poc.model.TripDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class TripDetailsDao {
    private static final String INSERT_TRIP_DETAILS_QUERY = "INSERT INTO trip_details (date, outlet_code, vehicle_number,num_cases_imfl,num_cases_beer,form_3) VALUES (:date, :outletCode, :vehicleNumber, :imfl, :beer, :form3)";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    public void saveTripDetails(TripDetails tripDetails) {
        log.info("Saving trip details...");
        List<TripDetail> recordObjects = convertToObjectList(tripDetails);

        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(recordObjects.toArray());
        namedParameterJdbcTemplate.batchUpdate(INSERT_TRIP_DETAILS_QUERY, params);
        log.info("Successfully inserted trip details for the Date : {}", tripDetails.getDate());
    }

    private List<TripDetail> convertToObjectList(TripDetails tripDetails) {
        log.info("Converting list of list of objects to TripDetail objects list...");
        LocalDate date = tripDetails.getDate();
        log.info("Date : {}", date);
        // Prepare record objects
        List<TripDetail> recordObjects = Lists.newArrayList();
        for (List<Object> record : tripDetails.getTripRecords()) {
            if (record.size() != 5) {
                throw new InvalidInputException("Invalid Trip Detail. Correct format is : " + "['5014', 'AP37Y9689', 0, 80, 0]");
            }
            String outletCode = (String) record.get(0);
            String vehicleNumber = (String) record.get(1);
            Integer imfl = (Integer) record.get(2);
            Integer beer = (Integer) record.get(3);
            Integer form3 = (Integer) record.get(4);

            recordObjects.add(new TripDetail(date, outletCode, vehicleNumber, imfl, beer, form3));
        }
        log.info("Trip Detail records : ");
        log.info(recordObjects.toString());

        return recordObjects;
    }
}
