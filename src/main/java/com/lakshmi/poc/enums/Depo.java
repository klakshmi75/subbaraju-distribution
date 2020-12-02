package com.lakshmi.poc.enums;

import lombok.Getter;

@Getter
public enum Depo {
    // TODO: If required get these details from DB
    CHAGALLU("Chagallu", "CHG", "West Godavari", 1),
    ELURU("Eluru", "ELR", "West Godavari", 2),
    BHIMAVARAM("Bhimavaram", "BVRM", "West Godavari", 3);

    private String depoName;
    private String depoAbbr;
    private String district;
    private int depoCode;

    private Depo(String depoName, String depoAbbr, String district, int depoCode) {
        this.depoName = depoName;
        this.depoAbbr = depoAbbr;
        this.district = district;
        this.depoCode = depoCode;
    }
}
