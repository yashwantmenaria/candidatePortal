package com.example.candidateportal.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.candidateportal.entity.Employee;
import com.example.candidateportal.entity.GatePass;
import com.example.candidateportal.entity.User;
import com.example.candidateportal.repository.EmployeeRepository;
import com.example.candidateportal.repository.GatePassRepository;
import com.example.candidateportal.repository.UserRepository;
import com.example.candidateportal.utils.EmailService;

@Service
public class GatePassService {

    @Autowired
    private GatePassRepository repo;
    
    @Autowired
    private UserRepository useRepo;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    public String applyGatePass(Long empId, Long managerId, String reason) {

        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();

        long count = repo.countMonthlyRequests(empId, month, year);

        if (count >= 2) {
            return "You have reached monthly limit (2 requests)";
        }

        GatePass gp = new GatePass();
        gp.setEmployeeId(empId);
        gp.setManagerId(managerId);
        gp.setReason(reason);
        gp.setStatus("PENDING");
        gp.setRequestDate(LocalDateTime.now());

        repo.save(gp);
        
        String managerEmail = getManagerEmail(managerId);

        String subject = "Gate Pass Request Approval Needed";
        String body = "Employee ID: " + empId +
                      "\nReason: " + reason +
                      "\nStatus: PENDING";

        emailService.sendMailForGatePass(managerEmail, subject, body);

        return "Request submitted successfully";
    }

    public String getManagerEmail(Long managerId) {
        
    	Optional<User> findById = useRepo.findById(managerId);
    	if(!findById.isEmpty()) {
    		return findById.get().getEmail();
    	}
        return null; 
    }
    
	  public String approveGatePass(Long id, String status) {

        GatePass gp = repo.findById(id).orElseThrow();

        gp.setStatus(status); // APPROVED / REJECTED
        gp.setApprovedDate(LocalDateTime.now());

        repo.save(gp);

        return "Updated Successfully";
    }

	  public List<GatePass> getManagerRequests(Long managerId) {

		    int month = LocalDate.now().getMonthValue();
		    int year = LocalDate.now().getYear();

		    return repo.findManagerMonthlyRequests(managerId, month, year);
		}
	  
	  public Page<GatePass> getGatePasses(String status, int page) {

	        // Logged-in manager ka email
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String email = auth.getName();

	        // Manager employee record
	        Employee manager = employeeRepository.findByEmailAndIsActiveTrue(email)
	                .orElseThrow(() -> new RuntimeException("Manager not found"));

	        Pageable pageable = PageRequest.of(
	                page,
	                10,
	                Sort.by("id").descending()
	        );

	        // Sirf usi manager ke under aayi requests dikhengi
	        if (status != null && !status.trim().isEmpty()) {
	            return repo.findByManagerIdAndStatusIgnoreCase(
	                    manager.getId(),
	                    status,
	                    pageable
	            );
	        }

	        return repo.findByManagerId(
	                manager.getId(),
	                pageable
	        );
	    }
	}
