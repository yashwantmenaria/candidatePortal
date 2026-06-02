package com.example.candidateportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
// @RequestMapping("/gatepass")
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
    public String approve(@RequestParam Long id,
                          @RequestParam String status) {
        return service.approveGatePass(id, status);
    }
    
    @GetMapping("/manager/{managerId}")
    public List<GatePass> managerDashboard(@PathVariable Long managerId) {
        return service.getManagerRequests(managerId);
    }
    
    @PutMapping("/{id}/status")
    public String approveGatePass(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return service.approveGatePass(id, status);
    }
  
}