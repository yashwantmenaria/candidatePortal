package com.example.candidateportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.candidateportal.entity.GatePass;
import com.example.candidateportal.service.GatePassService;

@RestController
@RequestMapping("/api/gatepass")
public class GatePassController {

    @Autowired
    private GatePassService service;

    @PostMapping("/apply")
    public String apply(@RequestParam Long empId,
                        @RequestParam Long managerId,
                        @RequestParam String reason) {
        return service.applyGatePass(empId, managerId, reason);
    }

    @PostMapping("/approve")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public String approve(@RequestParam Long id,
                          @RequestParam String status) {
        return service.approveGatePass(id, status);
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public String approveGatePass(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return service.approveGatePass(id, status);
    }
  
    @GetMapping("/manager-requests_action")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<Page<GatePass>> getGatePasses(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page) {

        Page<GatePass> gatePasses = service.getGatePasses(status, page);

        return ResponseEntity.ok(gatePasses);
    }
    
    @GetMapping("/my-requests_status")
    public ResponseEntity<Page<GatePass>> getMyGatePasses(
            @RequestParam(defaultValue = "0") int page) {

        return ResponseEntity.ok(service.getMyGatePasses(page));
    }
}