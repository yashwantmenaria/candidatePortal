package com.example.candidateportal.dto;

import lombok.Data;

@Data
public class AssignManagerRequest {
    private Long employeeId; // Jis employee ko manager assign karna hai
    private Long managerId;  // Selected manager ki ID
}