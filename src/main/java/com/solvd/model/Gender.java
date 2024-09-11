package com.solvd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    FEMALE("female"),
    MALE("male");

    private final String gender;
}
