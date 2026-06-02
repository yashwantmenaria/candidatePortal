package com.example.candidateportal.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.candidateportal.dto.AssignManagerRequest;
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
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {

		String email = principal.getName(); // JWT se aayega

		String response = employeeService.changePassword(email, request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/update-profile")
	public ResponseEntity<?> updateProfile(@RequestBody EmployeeDto request, Principal principal) {

		String email = principal.getName();

		return ResponseEntity.ok(employeeService.updateProfile(email, request));
	}

	@PostMapping("/assign-manager")
	public String assignManager(@RequestBody AssignManagerRequest request) {
		return employeeService.assignManager(request);
	}

	@GetMapping
	public Page<Employee> getAllEmployees(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortDir, @RequestParam(required = false) String search) {
		return employeeService.getAllEmployees(page, size, sortBy, sortDir, search);
	}

	@DeleteMapping("/{id}")
	public String deleteEmployee(@PathVariable Long id) {
		return employeeService.deleteEmployee(id);
	}

	// GET /api/employees/5
	@GetMapping("/{id}")
	public Employee getEmployeeById(@PathVariable Long id) {
		return employeeService.getEmployeeById(id);
	}

	@GetMapping("/managerList")
	public List<Employee> allManagerList() {
		return employeeService.allManagerList();
	}

	@GetMapping("/getEmpListUnderManager")
	public List<Employee> getEmpListUnderManager(Principal principal) {
		System.err.println("email >> "+ principal);
		return employeeService.getEmpListUnderManager(principal);
	}


}
