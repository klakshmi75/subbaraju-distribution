package com.lakshmi.poc.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String formatDateForFileName(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("d-MM"));
    }


}
