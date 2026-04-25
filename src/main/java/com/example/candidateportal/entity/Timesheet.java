package com.example.candidateportal.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"employeeId", "workDate"})
})
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private Long managerId;

    private LocalDate workDate;

    private Double hoursWorked;

    private String taskDescription;

    private String status; // PENDING, APPROVED, REJECTED

    private String managerComment;

    private LocalDate submittedDate;

    private LocalDate actionDate;

    // getters setters
}