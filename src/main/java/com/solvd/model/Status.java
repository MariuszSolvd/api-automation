package com.solvd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    @JsonProperty("active")
    ACTIVE("active"),
    @JsonProperty("inactive")
    INACTIVE("inactive");

    private final String status;
}
