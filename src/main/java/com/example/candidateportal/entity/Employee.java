package com.example.candidateportal.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;
	private String lastName;

	@Column(unique = true)
	private String email;

	private String phone;
	private String department;
	private LocalDate joiningDate;
	private String designation;

	private String gender;
	private String maritalStatus;

	private String salary;

	private LocalDate dob;

	@Column(unique = true)
	private String aadhaar;

	@Column(unique = true)
	private String pan;

	@Column(unique = true)
	private String passport;

	private String permanentAddress;
	private String currentAddress;

	@Column(unique = true)
	private String employeeId;

	private String employment;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "manager_id")
	private Employee manager;

	private String qualification;
	private String university;
	private String passingYear;

	private String bankName;
	private String accountNumber;
	private String ifscCode;

	private boolean profileCompleted = false;

}