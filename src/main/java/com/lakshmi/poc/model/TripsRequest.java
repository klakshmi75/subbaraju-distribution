package com.lakshmi.poc.model;

import com.lakshmi.poc.Depo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel("Trips Request Query Params")
@Getter
@Setter
@NoArgsConstructor
public class TripsRequest {
    @ApiModelProperty(value = "Depo Name. Ex: Chagallu/Eluru/Bhimavaram.", required = true)
    @NotNull(message = "Depo is mandatory")
    private Depo depo;

    @ApiModelProperty(value = "Date. Ex: 21/12/2020.", required = false)
    private LocalDate date;

    @ApiModelProperty(value = "If request is for a date range. Ex: true/false", example = "false", required = true)
    private boolean dateRange = false;

    @ApiModelProperty(value = "Start Date. Ex: 21/12/2020.", required = false)
    private LocalDate startDate;

    @ApiModelProperty(value = "End Date. Ex: 21/12/2020.", required = false)
    private LocalDate endDate;

    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "startDate and endDate are mandatory and date should be null when dateRange is true")
    public boolean getDateRangeTrueValidation() {
        boolean result = true;
        if (dateRange && (startDate == null || endDate == null)) {
            result = false;
        }
        if (dateRange && date != null) {
            result = false;
        }
        return result;
    }

    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "date is mandatory and startDate and endDate should be null when dateRange is false")
    public boolean getDateRangeFalseValidation() {
        boolean result = true;
        if (!dateRange && date == null) {
            result = false;
        }
        if (!dateRange && (startDate != null || endDate != null)) {
            result = false;
        }
        return result;
    }

    @ApiModelProperty(hidden = true)
    @AssertTrue(message = "endDate cannot be after startDate")
    public boolean getStartDateNotAfterEndDateValidation() {
        boolean result = true;
        if (dateRange && startDate != null && endDate != null && startDate.isAfter(endDate)) {
            result = false;
        }
        return result;
    }

}
