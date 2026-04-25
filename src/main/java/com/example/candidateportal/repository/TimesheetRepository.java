package com.example.candidateportal.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.candidateportal.entity.Timesheet;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    Optional<Timesheet> findByEmployeeIdAndWorkDate(Long empId, LocalDate date);

    List<Timesheet> findByEmployeeId(Long empId);

    List<Timesheet> findByManagerIdAndStatus(Long managerId, String status);

    List<Timesheet> findByManagerId(Long managerId);
    
    // Employee monthly data
    List<Timesheet> findByEmployeeIdAndWorkDateBetween(
            Long empId, LocalDate start, LocalDate end
    );

    // Manager team monthly data
    List<Timesheet> findByManagerIdAndWorkDateBetween(
            Long managerId, LocalDate start, LocalDate end
    );
}