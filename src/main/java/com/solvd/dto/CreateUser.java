package com.solvd.dto;

import com.solvd.model.Gender;
import com.solvd.model.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUser {

    private final String name;
    private String email;
    private final Gender gender;
    private final Status status;
}
