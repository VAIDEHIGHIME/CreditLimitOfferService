package com.vaidehighime.creditlimitservice.models;

public enum CreditLimitOfferStatus {
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    PENDING("PENDING");

    private String value;

    private CreditLimitOfferStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
