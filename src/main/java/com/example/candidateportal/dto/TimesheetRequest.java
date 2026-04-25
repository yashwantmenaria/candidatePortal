package com.example.candidateportal.dto;

import java.time.LocalDate;

import org.antlr.v4.runtime.misc.NotNull;

import lombok.Data;

@Data
public class TimesheetRequest {

    @NotNull
    private LocalDate workDate;

    @NotNull
    private Double hoursWorked;

    @NotNull
    private String taskDescription;

    // getters setters
}