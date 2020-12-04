package com.lakshmi.poc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel("Sigma Billing Trip Detail")
public class TripDetail {
    @ApiModelProperty(value = "Date. Ex: 21/12/2020.", example = "21/11/2020", required = true)
    @NotNull(message = "Date is mandatory")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @ApiModelProperty(value = "Outlet Code. Ex: 5014.", example = "5014", required = true)
    @NotNull(message = "Outlet Code is mandatory")
    private String outletCode;

    @ApiModelProperty(value = "Vehicle Number. Ex: AP37Y9689.", example = "AP37Y9689", required = true)
    @NotNull(message = "Vehicle NUmber is mandatory")
    private String vehicleNumber;

    @ApiModelProperty(value = "IMFL Cases. Ex: 100.", example = "0", required = true)
    @NotNull(message = "IMFL cases is mandatory")
    private Integer imfl;

    @ApiModelProperty(value = "BEER Cases. Ex: 80.", example = "0", required = true)
    @NotNull(message = "BEER Cases is mandatory")
    private Integer beer;

    @ApiModelProperty(value = "Form-3 in Amount. Ex: 230.", example = "0", required = false)
    private Integer form3 = 0;
}
