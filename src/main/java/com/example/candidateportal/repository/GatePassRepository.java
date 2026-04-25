package com.example.candidateportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.candidateportal.entity.GatePass;

public interface GatePassRepository extends JpaRepository<GatePass, Long> {

    @Query("SELECT COUNT(g) FROM GatePass g WHERE g.employeeId = :empId AND MONTH(g.requestDate) = :month AND YEAR(g.requestDate) = :year")
    long countMonthlyRequests(Long empId, int month, int year);

    @Query("SELECT g FROM GatePass g WHERE g.managerId = :managerId AND MONTH(g.requestDate) = :month AND YEAR(g.requestDate) = :year")
    List<GatePass> findManagerMonthlyRequests(Long managerId, int month, int year);
}