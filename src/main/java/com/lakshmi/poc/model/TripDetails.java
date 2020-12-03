package com.lakshmi.poc.model;

import com.lakshmi.poc.enums.Depo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@ApiModel("Trips Request Query Params")
@Getter
@Setter
@NoArgsConstructor
public class TripDetails {
    @ApiModelProperty(value = "Date. Ex: 21/12/2020.", required = true)
    @NotNull(message = "Date is mandatory")
    private LocalDate date;

    @ApiModelProperty(value = "Trip Records. Ex: [['5014', 'AP37Y9689', 0, 80, 0],['5015', 'AP37Y9769', 20, 60, 150]]", required = true)
    @NotNull(message = "tripRecords are mandatory")
    private List<List<Object>> tripRecords;
}
