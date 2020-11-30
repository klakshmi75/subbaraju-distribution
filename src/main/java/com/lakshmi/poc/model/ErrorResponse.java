package com.lakshmi.poc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Error")
public class ErrorResponse {
    @ApiModelProperty(notes = "Message regarding request execution.", required = true)
    private String code;
    @ApiModelProperty(notes = "List of errors occurred dut=ring request execution.", required = true)
    private List<String> errors;
}
