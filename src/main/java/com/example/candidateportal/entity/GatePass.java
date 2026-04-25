package com.example.candidateportal.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class GatePass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;
    private Long managerId;
    private String reason;

    private String status; // PENDING, APPROVED, REJECTED

    private LocalDateTime requestDate;
    private LocalDateTime approvedDate;
}