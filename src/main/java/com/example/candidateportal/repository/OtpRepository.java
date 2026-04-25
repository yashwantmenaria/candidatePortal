package com.example.candidateportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candidateportal.entity.OtpEntity;

public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findByEmail(String email);
}