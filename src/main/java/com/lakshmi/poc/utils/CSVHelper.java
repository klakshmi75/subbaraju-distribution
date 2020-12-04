package com.lakshmi.poc.utils;

import com.lakshmi.poc.exception.InvalidInputException;
import com.lakshmi.poc.model.TripDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CSVHelper {
    public static boolean hasCSVFormat(MultipartFile file) {

        if (!MediaType.APPLICATION_OCTET_STREAM_VALUE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<TripDetail> csvToTripDetails(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<TripDetail> tripDetails = new ArrayList<TripDetail>();
            log.info("Parsing csv file...");
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                if(csvRecord.size() != 6) {
                    throw new InvalidInputException("Some of the records has missing fields. Please correct the file and upload again!");
                }
                TripDetail tripDetail = new TripDetail(
                        DateUtils.getLocalDateFromString(csvRecord.get("Date").trim()),
                        csvRecord.get("Outlet Code").trim(),
                        csvRecord.get("Vehicle Number").trim(),
                        Integer.parseInt(csvRecord.get("IMFL").trim()),
                        Integer.parseInt(csvRecord.get("BEER").trim()),
                        Integer.parseInt(csvRecord.get("Form-3").trim())
                );
                log.debug(tripDetail.toString());
                tripDetails.add(tripDetail);
            }
            log.info("Number of records in CSV : {}", tripDetails.size());
            return tripDetails;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}