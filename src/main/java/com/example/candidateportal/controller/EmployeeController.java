package com.example.candidateportal.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.candidateportal.dto.ChangePasswordRequest;
import com.example.candidateportal.dto.EmployeeDto;
import com.example.candidateportal.entity.Employee;
import com.example.candidateportal.service.UserService;

@RestController
public class EmployeeController {
	
	@Autowired
	UserService employeeService;

	@PostMapping("/onboard")
	@PreAuthorize("hasRole('HR')")
	public Employee onboard(@RequestBody EmployeeDto dto) {
		return employeeService.onboard(dto);
	}

	 @PostMapping("/change-password")
	    public ResponseEntity<?> changePassword(
	            @RequestBody ChangePasswordRequest request,
	            Principal principal) {

	        String email = principal.getName(); // JWT se aayega

	        String response = employeeService.changePassword(email, request);

	        return ResponseEntity.ok(response);
	    }
	 
	 @PostMapping("/update-profile")
	 public ResponseEntity<?> updateProfile(
	         @RequestBody EmployeeDto request,
	         Principal principal) {

	     String email = principal.getName();

	     return ResponseEntity.ok(employeeService.updateProfile(email, request));
	 }
	
}
