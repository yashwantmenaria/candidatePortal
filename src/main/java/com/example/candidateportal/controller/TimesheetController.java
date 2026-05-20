package com.example.candidateportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.candidateportal.dto.TimesheetRequest;
import com.example.candidateportal.service.TimesheetService;

@RestController
@CrossOrigin(origins = "*") // frontend allow
@RequestMapping("/api/timesheet")
public class TimesheetController {

    @Autowired
    private TimesheetService service;

    // 🟢 Add
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam Long employeeId,
            @RequestParam Long managerId,
            @RequestBody TimesheetRequest req) {
        return ResponseEntity.ok(
                service.addEntry(employeeId, managerId, req)
        );
    }

    // 🟡 Update
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody TimesheetRequest req) {
        return ResponseEntity.ok(service.updateEntry(id, req));
    }

    // 🔵 My Timesheet
    @GetMapping("/my")
    public ResponseEntity<?> my(@RequestParam Long employeeId) {
        return ResponseEntity.ok(
                service.getMyTimesheet(employeeId)
        );
    }

    // 🟠 Manager Pending
    @GetMapping("/manager/pending")
    public ResponseEntity<?> pending( @RequestParam Long managerId) {
        return ResponseEntity.ok(
                service.getPendingForManager(managerId)
        );
    }

    // ✅ Approve
    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable Long id,
    		 @RequestParam Long managerId) {
        return ResponseEntity.ok(
                service.approve(id, managerId)
        );
    }

    // ❌ Reject
    @PostMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id,
                                    @RequestParam String comment, @RequestParam Long managerId) {
        return ResponseEntity.ok(
                service.reject(id, managerId, comment)
        );
    }
    
    @GetMapping("/employee")
    public ResponseEntity<?> employeeReport(
            @RequestParam Long employeeId,
            @RequestParam int month,
            @RequestParam int year) {

        return ResponseEntity.ok(
                service.getEmployeeMonthlyReport(employeeId, month, year)
        );
    }

    // 👨‍💼 Manager report
    @GetMapping("/manager")
    public ResponseEntity<?> managerReport(
            @RequestParam Long managerId,
            @RequestParam int month,
            @RequestParam int year) {

        return ResponseEntity.ok(
                service.getManagerMonthlyReport(managerId, month, year)
        );
    }
}