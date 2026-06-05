package com.example.candidateportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candidateportal.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmail(String email);

	List<Employee> findByManagerIsNotNull();

	Optional<Employee> findByIdAndIsActiveTrue(Long id);

	Page<Employee> findAll(Specification<Employee> specification, Pageable pageable);

	Optional<Employee> findByEmailAndIsActiveTrue(String email);

	List<Employee> findByManagerIdIsNotNull(Employee employee);

}