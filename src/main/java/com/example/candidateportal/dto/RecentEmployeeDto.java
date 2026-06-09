package com.example.candidateportal.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecentEmployeeDto {

    private String employeeId;
    private String name;
    private String email;
    private String department;
    private String designation;
    private LocalDate joiningDate;
}