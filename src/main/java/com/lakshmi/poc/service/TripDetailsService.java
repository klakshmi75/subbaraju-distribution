package com.lakshmi.poc.service;

import com.lakshmi.poc.dao.TripDetailsDao;
import com.lakshmi.poc.model.TripDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
}
