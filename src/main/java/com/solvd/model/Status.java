package com.solvd.model;

public enum Status {

    ACTIVE("active"),
    INACTIVE("inactive");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
