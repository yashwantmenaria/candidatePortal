package com.example.candidateportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candidateportal.entity.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
}