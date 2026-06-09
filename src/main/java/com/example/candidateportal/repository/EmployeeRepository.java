package com.example.candidateportal.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.candidateportal.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmail(String email);

	List<Employee> findByManagerIsNotNull();

	Optional<Employee> findByIdAndIsActiveTrue(Long id);

	Page<Employee> findAll(Specification<Employee> specification, Pageable pageable);

	Optional<Employee> findByEmailAndIsActiveTrue(String email);

	List<Employee> findByManager_Id(Long managerId);

	List<Employee> findTop5ByOrderByJoiningDateDesc();
	
	@Query(value = """
		    SELECT *
		    FROM employee e
		    WHERE MONTH(e.dob) = MONTH(CURDATE())
		    AND DAY(e.dob) = DAY(CURDATE())
		    """, nativeQuery = true)
		List<Employee> findTodaysBirthdays();
	
	@Query(value = """
		    SELECT *
		    FROM employee e
		    WHERE e.dob IS NOT NULL
		      AND DATE_FORMAT(e.dob, '%m-%d')
		          > DATE_FORMAT(CURDATE(), '%m-%d')
		    ORDER BY DATE_FORMAT(e.dob, '%m-%d')
		    LIMIT 2
		    """, nativeQuery = true)
		List<Employee> findUpcomingBirthdays();

	@Query(value = """
		    SELECT *
		    FROM employee e
		    WHERE e.dob IS NOT NULL
		    ORDER BY DATE_FORMAT(e.dob, '%m-%d')
		    LIMIT 2
		    """, nativeQuery = true)
		List<Employee> findUpcomingBirthdaysFromStartOfYear();}