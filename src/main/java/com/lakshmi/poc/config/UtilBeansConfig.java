package com.lakshmi.poc.config;

import com.lakshmi.poc.constants.RequestConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
@Configuration
public class UtilBeansConfig {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(RequestConstants.DATE_FORMAT);
    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {

            @Override
            public LocalDate parse(String text, Locale locale) throws ParseException {
                return LocalDate.parse(text, DATE_TIME_FORMATTER);
            }

            @Override
            public String print(LocalDate object, Locale locale) {
                return DATE_TIME_FORMATTER.format(object);
            }
        };
    }
}
