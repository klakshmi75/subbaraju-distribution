package com.lakshmi.poc.controller;

import com.lakshmi.poc.service.ReportsService;
import com.lakshmi.poc.model.SigmaBillingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Slf4j
public class TripsController {
    @Autowired
    ReportsService reportsService;

    private static final String MIME_TYPE = "application/octet-stream";

    @RequestMapping(value = "/trips", method = RequestMethod.GET, produces = "application/json")
    public void firstPage(@Valid SigmaBillingRequest sigmaBillingRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("TripsRequest\n {}", sigmaBillingRequest);

        response.setContentType(MIME_TYPE);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + reportsService.getSigmaBillingFileName(sigmaBillingRequest) + "\""));

        reportsService.generateSigmaBillingReport(sigmaBillingRequest, response.getOutputStream());
    }
}
