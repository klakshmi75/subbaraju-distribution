package com.lakshmi.poc.dao;

import com.lakshmi.poc.model.TripDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TripDetailsDao {
    private static final String INSERT_TRIP_DETAILS_QUERY = "INSERT INTO trip_details (date, outlet_code, vehicle_number,num_cases_imfl,num_cases_beer,form_3) VALUES (:date, :outletCode, :vehicleNumber, :imfl, :beer, :form3)";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void saveTripDetails(List<TripDetail> beanList) {
        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(beanList.toArray());
        namedParameterJdbcTemplate.batchUpdate(INSERT_TRIP_DETAILS_QUERY, params);
    }

}
