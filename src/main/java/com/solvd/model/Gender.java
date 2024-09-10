package com.solvd.model;

public enum Gender {

    FEMALE("female"),
    MALE("male");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }
}
