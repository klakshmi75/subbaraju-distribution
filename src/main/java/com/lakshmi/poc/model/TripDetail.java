package com.lakshmi.poc.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TripDetail {
    private LocalDate date;
    private String outletCode;
    private String vehicleNumber;
    private Integer imfl;
    private Integer beer;
    private Integer form3;
}
