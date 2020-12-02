package com.lakshmi.poc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lakshmi.poc.exception.WorkbookDetailsParseException;
import com.lakshmi.poc.pojo.WorkbookDetails;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class ExcelFormatUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final String SIGMA_BILLING_DETAILS_JSON = "json/BillingDetailsFormat.json";

    public static WorkbookDetails getSigmaBillingDetailsFormat() {

        return getExcelDetailsFromJson(SIGMA_BILLING_DETAILS_JSON);
    }

    private static WorkbookDetails getExcelDetailsFromJson(String jsonFilePath) {
        WorkbookDetails details;
        try {
            log.info("Reading Excel Details json file : {}...", jsonFilePath);
            File file = getFileFromResources(jsonFilePath);
            log.info("Reading json file {} content as String...", jsonFilePath);
            String content = getStringFromFile(file);
            log.info(content);
            log.info("Parsing json to object...");
            details = objectMapper.readValue(content, WorkbookDetails.class);
            log.info("Successfully parsed json file {} to object.", jsonFilePath);
        } catch (Exception e) {
            throw new WorkbookDetailsParseException("Exception while parsing excel details", e);
        }

        return details;
    }

    private static File getFileFromResources(String jsonFilePath) {
        ClassLoader classLoader = ExcelFormatUtils.class.getClassLoader();
        File file = new File(classLoader.getResource(jsonFilePath).getFile());

        if (!file.exists()) {
            throw new WorkbookDetailsParseException("Excel Details json file does not exist : " + jsonFilePath);
        }

        return file;
    }

    private static String getStringFromFile(File file) throws IOException {

        return new String(Files.readAllBytes(file.toPath()));
    }

}
