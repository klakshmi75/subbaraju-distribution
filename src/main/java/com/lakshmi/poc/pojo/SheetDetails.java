package com.lakshmi.poc.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SheetDetails {
    private String sheetName;
    private String queryByDate;
    private String queryByDateRange;
    private List<ColumnDetails> columnsDetailsList;
    private boolean addDepoHeader;
}
