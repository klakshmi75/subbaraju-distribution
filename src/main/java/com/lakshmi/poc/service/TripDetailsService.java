package com.lakshmi.poc.service;

import com.google.common.collect.Lists;
import com.lakshmi.poc.dao.TripDetailsDao;
import com.lakshmi.poc.exception.InvalidInputException;
import com.lakshmi.poc.model.TripDetail;
import com.lakshmi.poc.model.TripDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class TripDetailsService {
    @Autowired
    private TripDetailsDao tripDetailsDao;

    @Transactional
    public void saveTripDetails(List<TripDetail> tripDetailList) {
        tripDetailsDao.saveTripDetails(tripDetailList);
    }

    public List<TripDetail> convertToBeanList(TripDetails tripDetails) {
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
