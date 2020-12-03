package com.lakshmi.poc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lakshmi.poc.enums.Depo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@ApiModel("Trip Details for a date")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TripDetails {
    @ApiModelProperty(value = "Date. Ex: 21/12/2020.", example="21/11/2020", required = true)
    @NotNull(message = "Date is mandatory")
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate date;

    @ApiModelProperty(value = "Trip Records. Ex: [[\"5014\", \"AP37Y9689\", 0, 80, 0],[\"5015\", \"AP37Y9769\", 20, 60, 150]]", required = true)
    @NotNull(message = "tripRecords are mandatory")
    private List<List<Object>> tripRecords;
}
