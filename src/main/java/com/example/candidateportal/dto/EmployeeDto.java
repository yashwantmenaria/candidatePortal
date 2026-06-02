package com.example.candidateportal.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeDto {
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String department;
	private LocalDate joiningDate;
	private String designation;
	private String gender;
	private String maritalStatus;
	private String salary;
	private LocalDate dob;
	private String aadhaar;
	private String pan;
	private String passport;
	private String permanentAddress;
	private String currentAddress;
	private String employment;
	private String qualification;
	private String university;
	private String passingYear;
	private String bankName;
	private String accountNumber;
	private String ifscCode;
	private boolean profileCompleted = false;
    private Long managerId;
	private String role;

}