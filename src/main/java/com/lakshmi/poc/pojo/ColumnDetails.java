package com.lakshmi.poc.pojo;

import com.lakshmi.poc.enums.Alignment;
import com.lakshmi.poc.enums.ColumnDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColumnDetails {
    private String header;
    private ColumnDataType type;
    private boolean addTotal;
}
