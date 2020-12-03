package com.lakshmi.poc.controller;

import com.lakshmi.poc.exception.CustomException;
import com.lakshmi.poc.service.ReportsService;
import com.lakshmi.poc.model.SigmaBillingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
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
public class SigmaBillingController {
    @Autowired
    ReportsService reportsService;

    @RequestMapping(value = "/billing/sigma", method = RequestMethod.GET, produces = "application/json")
    public void firstPage(@Valid SigmaBillingRequest sigmaBillingRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("TripsRequest\n {}", sigmaBillingRequest);
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + reportsService.getSigmaBillingFileName(sigmaBillingRequest) + "\""));
            reportsService.generateSigmaBillingReport(sigmaBillingRequest, response.getOutputStream());
        } catch (Exception e) {
            // Some exception occurred while preparing excel
            // Set content type as json and throw custom exception
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            throw new CustomException("Some exception occurred while preparing excel. Check server logs.", e);
        }

    }
}
