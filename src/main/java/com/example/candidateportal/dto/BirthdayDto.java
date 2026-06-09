package com.example.candidateportal.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BirthdayDto {

    private String employeeId;
    private String name;
    private String department;
    private String email;
    private LocalDate dob;
}