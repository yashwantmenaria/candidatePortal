package com.example.candidateportal.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.candidateportal.dto.AssignManagerRequest;
import com.example.candidateportal.dto.ChangePasswordRequest;
import com.example.candidateportal.dto.EmployeeDto;
import com.example.candidateportal.entity.Employee;
import com.example.candidateportal.entity.Role;
import com.example.candidateportal.entity.User;
import com.example.candidateportal.repository.EmployeeRepository;
import com.example.candidateportal.repository.RoleRepository;
import com.example.candidateportal.repository.UserRepository;
import com.example.candidateportal.utils.EmailService;

import jakarta.persistence.criteria.Predicate;

@Service
public class UserService {

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private EmployeeRepository employeeRepo;

	@Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
	@Autowired
	EmailService emailService;

    public Employee onboard(EmployeeDto dto) {
		// 1. create user
		User user = new User();
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode("temp123")); // default password
		String token = UUID.randomUUID().toString();
		user.setVerificationToken(token);
		
		Role empRole = roleRepo.findByName(dto.getRole()).orElseThrow();

		user.setRoles(Set.of(empRole));
		userRepo.save(user);

		// 2. create employee
		Employee emp = new Employee();
		emp.setFirstName(dto.getFirstName());
		emp.setLastName(dto.getLastName());
		emp.setEmail(dto.getEmail());
		emp.setPhone(dto.getPhone());
		emp.setDepartment(dto.getDepartment());
		emp.setJoiningDate(dto.getJoiningDate());
		emp.setDesignation(dto.getDesignation());
		emp.setEmployeeId(generateEmployeeId());
		emp.setUser(user);
		if (dto.getManagerId() != null) {
			Employee manager = employeeRepo.findById(dto.getManagerId())
					.orElseThrow(() -> new RuntimeException("Manager not found"));
			emp.setManager(manager);
		}
		 Employee save = employeeRepo.save(emp);
		
		String tempPassword = "temp123"; // ya random generate karo

		emailService.sendOnboardingMail(
		        emp.getEmail(),
		        emp.getFirstName(),
		        emp.getEmployeeId(),
		        tempPassword
		);
		
		return save;
		
    }
    
    public String generateEmployeeId() {
		long count = employeeRepo.count() + 1;
		return String.format("EMP%03d", count);
	}
    
    
    public String changePassword(String email, ChangePasswordRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1. Old password check
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // 2. New & confirm password match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        // 3. Same password check
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be same as old password");
        }

        // 4. Save new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Password changed successfully";
    }

    public String updateProfile(String email, EmployeeDto request) {

        Employee emp = employeeRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        emp.setQualification(request.getQualification());
        emp.setUniversity(request.getUniversity());
        emp.setPassingYear(request.getPassingYear());

        emp.setBankName(request.getBankName());
        emp.setAccountNumber(request.getAccountNumber());
        emp.setIfscCode(request.getIfscCode());

        emp.setCurrentAddress(request.getCurrentAddress());
        emp.setPermanentAddress(request.getPermanentAddress());
        
        emp.setAadhaar(request.getAadhaar());
        emp.setPan(request.getPan());
        emp.setPassport(request.getPassport());
        
        emp.setProfileCompleted(true);
        emp.setGender(request.getGender());
        emp.setMaritalStatus(request.getMaritalStatus());
        emp.setDob(request.getDob());
        
        employeeRepo.save(emp);

        return "Profile updated successfully";
    }

	public List<Employee> allManagerList() {
        return employeeRepo.findByManagerIsNotNull();
	}
	
	  public String assignManager(AssignManagerRequest request) {

	        Employee employee = employeeRepo.findById(request.getEmployeeId())
	                .orElseThrow(() -> new RuntimeException("Employee not found"));

	        Employee manager = employeeRepo.findById(request.getManagerId())
	                .orElseThrow(() -> new RuntimeException("Manager not found"));

	        if (employee.getId().equals(manager.getId())) {
	            throw new RuntimeException("Employee cannot be their own manager");
	        }

	        // Manager assign
	        employee.setManager(manager);

	        employeeRepo.save(employee);

	        return "Manager assigned successfully";
	    }
	  
	  public Page<Employee> getAllEmployees(
	            int page,
	            int size,
	            String sortBy,
	            String sortDir,
	            String search
	    ) {

	        Sort sort = sortDir.equalsIgnoreCase("desc")
	                ? Sort.by(sortBy).descending()
	                : Sort.by(sortBy).ascending();

	        Pageable pageable = PageRequest.of(page, size, sort);

	        Specification<Employee> specification = (root, query, cb) -> {
	            if (search == null || search.trim().isEmpty()) {
	                return cb.conjunction();
	            }

	            String keyword = "%" + search.toLowerCase() + "%";
	            List<Predicate> predicates = new ArrayList<>();

	            predicates.add(cb.like(cb.lower(root.get("firstName")), keyword));
	            predicates.add(cb.like(cb.lower(root.get("lastName")), keyword));
	            predicates.add(cb.like(cb.lower(root.get("email")), keyword));
	            predicates.add(cb.like(cb.lower(root.get("employeeId")), keyword));
	            predicates.add(cb.like(cb.lower(root.get("department")), keyword));

	            return cb.or(predicates.toArray(new Predicate[0]));
	        };

	        return employeeRepo.findAll(specification, pageable);
	    }
	  
	  public String deleteEmployee(Long id) {
	        Employee employee = employeeRepo.findByIdAndIsActiveTrue(id)
	                .orElseThrow(() -> new RuntimeException("Employee not found"));
	        // Soft delete
	        employee.setActive(false);
	        employee.setDeleted(true);
	        employeeRepo.save(employee);
	        return "Employee deleted successfully";
	    }
	  
	  public Employee getEmployeeById(Long id) {
	        return employeeRepo.findByIdAndIsActiveTrue(id)
	                .orElseThrow(() -> new RuntimeException("Employee not found"));
	    }

	  public List<Employee> getEmpListUnderManager(Principal principal) {

			String name = principal.getName();
			Employee emp = employeeRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
			return employeeRepo.findByManagerIdIsNotNull(emp.getId());

	}
}