package com.lakshmi.poc.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String formatDateForFileName(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("d-MM"));
    }

    public static LocalDate getLocalDateFromString(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
