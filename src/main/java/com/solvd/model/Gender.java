package com.solvd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    @JsonProperty("female")
    FEMALE("female"),
    @JsonProperty("male")
    MALE("male");

    private final String gender;
}
