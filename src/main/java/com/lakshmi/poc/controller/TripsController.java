package com.lakshmi.poc.controller;

import com.lakshmi.poc.exception.CustomException;
import com.lakshmi.poc.exception.InvalidInputException;
import com.lakshmi.poc.model.TripDetail;
import com.lakshmi.poc.service.ReportsService;
import com.lakshmi.poc.model.SigmaBillingRequest;
import com.lakshmi.poc.service.TripDetailsService;
import com.lakshmi.poc.utils.CSVHelper;
import com.lakshmi.poc.utils.ExcelHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.service.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Slf4j
public class TripsController {
    @Autowired
    ReportsService reportsService;

    @Autowired
    TripDetailsService tripDetailsService;

    @RequestMapping(value = "sigma/report", method = RequestMethod.GET, produces = "application/json")
    public void downloadingSigmaBillingReport(@Valid SigmaBillingRequest sigmaBillingRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
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

//    @RequestMapping(value = "/sigma/data/single", method = RequestMethod.POST, produces = "application/json")
    public String saveSingleForSigmaBilling(@Valid @RequestBody TripDetail tripDetail) {
        log.info("TripDetail\n {}", tripDetail);
        log.info("Saving trip details single for sigma billing...");
        tripDetailsService.saveTripDetails(Collections.singletonList(tripDetail));
        return "Single Trip detail saved successfully!!";
    }

//    @RequestMapping(value = "/sigma/data/list", method = RequestMethod.POST, produces = "application/json")
    public String saveListForSigmaBilling(@Valid @RequestBody List<TripDetail> tripDetailsList) {
        log.info("TripDetail List\n {}", tripDetailsList);
        log.info("Saving trip details list for sigma billing...");
        tripDetailsService.saveTripDetails(tripDetailsList);
        log.info("Successfully inserted trip details.") ;
        return tripDetailsList.size() + " Trip details saved successfully!!";
    }

//    @RequestMapping(value = "/sigma/data/csv", method = RequestMethod.POST, produces = "application/json")
    public String saveCSVForSigmaBilling(@RequestParam("file") MultipartFile file) {
        log.info("Processing CSV File with name : {}", file.getOriginalFilename());
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                List<TripDetail> tripDetails = CSVHelper.csvToTripDetails(file.getInputStream());
                tripDetailsService.saveTripDetails(tripDetails);
                log.info("CSV file uploaded successfully with ");
                return String.format("Uploaded the file successfully. Number of records : %d", tripDetails.size());
            } catch (Exception e) {
                throw new CustomException("Could not upload the file: " + file.getOriginalFilename() + "!", e);
            }
        } else {
            throw  new InvalidInputException("Invalid file format. Please upload a csv file!");
        }
    }

    @RequestMapping(value = "/sigma/data/excel", method = RequestMethod.POST, produces = "application/json")
    public String saveExcelForSigmaBilling(@RequestParam("file") MultipartFile file) {
        log.info("Processing Excel File with name : {}", file.getOriginalFilename());
        try {
            List<TripDetail> tripDetails = ExcelHelper.excelToTripDetails(file);
            tripDetailsService.saveTripDetails(tripDetails);
            log.info("Excel file uploaded successfully with ");
            return String.format("Uploaded the file successfully. Number of records : %d", tripDetails.size()) ;
        } catch (Exception e) {
            throw new CustomException("Could not upload the file: " + file.getOriginalFilename() + "!", e);
        }
    }

}
