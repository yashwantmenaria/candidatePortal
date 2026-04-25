package com.example.candidateportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candidateportal.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmail(String email);
}