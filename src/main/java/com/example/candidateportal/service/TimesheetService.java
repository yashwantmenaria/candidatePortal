package com.example.candidateportal.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.candidateportal.dto.TimesheetRequest;
import com.example.candidateportal.entity.Timesheet;
import com.example.candidateportal.repository.TimesheetRepository;
import com.example.candidateportal.response.MonthlyReportResponse;

@Service
public class TimesheetService {

    @Autowired
    private TimesheetRepository repo;

    // 🟢 Add Timesheet
    public Timesheet addEntry(Long empId, Long managerId, TimesheetRequest req) {

        // 7 days validation
        if (LocalDate.now().isAfter(req.getWorkDate().plusDays(7))) {
            throw new RuntimeException("7 days exp");
        }

        // duplicate check
        Optional<Timesheet> existing =
                repo.findByEmployeeIdAndWorkDate(empId, req.getWorkDate());

        if (existing.isPresent()) {
            throw new RuntimeException("Already filled for this date");
        }

        Timesheet ts = new Timesheet();
        ts.setEmployeeId(empId);
        ts.setManagerId(managerId);
        ts.setWorkDate(req.getWorkDate());
        ts.setHoursWorked(req.getHoursWorked());
        ts.setTaskDescription(req.getTaskDescription());
        ts.setStatus("PENDING");
        ts.setSubmittedDate(LocalDate.now());

        return repo.save(ts);
    }

    // 🟡 Update (only REJECTED)
    public Timesheet updateEntry(Long id, TimesheetRequest req) {
        Timesheet ts = repo.findById(id).orElseThrow();

        if (!ts.getStatus().equals("REJECTED")) {
            throw new RuntimeException("Only rejected can be edited");
        }

        ts.setHoursWorked(req.getHoursWorked());
        ts.setTaskDescription(req.getTaskDescription());

        ts.setStatus("PENDING");

        return repo.save(ts);
    }

    // 🔵 Employee View
    public List<Timesheet> getMyTimesheet(Long empId) {
        return repo.findByEmployeeId(empId);
    }

    // 🟠 Manager View (Pending)
    public List<Timesheet> getPendingForManager(Long managerId) {
        return repo.findByManagerIdAndStatus(managerId, "PENDING");
    }

    // ✅ Approve
    public Timesheet approve(Long id, Long managerId) {
        Timesheet ts = repo.findById(id).orElseThrow();

        if (!ts.getManagerId().equals(managerId)) {
            throw new RuntimeException("Unauthorized");
        }

        ts.setStatus("APPROVED");
        ts.setActionDate(LocalDate.now());

        return repo.save(ts);
    }

    // ❌ Reject
    public Timesheet reject(Long id, Long managerId, String comment) {
        Timesheet ts = repo.findById(id).orElseThrow();

        if (!ts.getManagerId().equals(managerId)) {
            throw new RuntimeException("Unauthorized");
        }

        ts.setStatus("REJECTED");
        ts.setManagerComment(comment);
        ts.setActionDate(LocalDate.now());

        return repo.save(ts);
    }
    
    public MonthlyReportResponse getEmployeeMonthlyReport(Long empId, int month, int year) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Timesheet> list =
                repo.findByEmployeeIdAndWorkDateBetween(empId, start, end);

        double total = list.stream()
                .mapToDouble(Timesheet::getHoursWorked)
                .sum();

        List<MonthlyReportResponse.DailyEntry> entries = list.stream()
                .map(ts -> {
                    MonthlyReportResponse.DailyEntry d = new MonthlyReportResponse.DailyEntry();
                    d.setDate(ts.getWorkDate());
                    d.setHours(ts.getHoursWorked());
                    d.setStatus(ts.getStatus());
                    return d;
                }).toList();

        MonthlyReportResponse res = new MonthlyReportResponse();
        res.setEmployeeId(empId);
        res.setTotalHours(total);
        res.setEntries(entries);

        return res;
    }
    
    public Map<Long, MonthlyReportResponse> getManagerMonthlyReport(Long managerId, int month, int year) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Timesheet> list =
                repo.findByManagerIdAndWorkDateBetween(managerId, start, end);

        // Group by employee
        Map<Long, List<Timesheet>> grouped =
                list.stream().collect(Collectors.groupingBy(Timesheet::getEmployeeId));

        Map<Long, MonthlyReportResponse> result = new HashMap<>();

        for (Long empId : grouped.keySet()) {

            List<Timesheet> empList = grouped.get(empId);

            double total = empList.stream()
                    .mapToDouble(Timesheet::getHoursWorked)
                    .sum();

            List<MonthlyReportResponse.DailyEntry> entries = empList.stream()
                    .map(ts -> {
                        MonthlyReportResponse.DailyEntry d = new MonthlyReportResponse.DailyEntry();
                        d.setDate(ts.getWorkDate());
                        d.setHours(ts.getHoursWorked());
                        d.setStatus(ts.getStatus());
                        return d;
                    }).toList();

            MonthlyReportResponse res = new MonthlyReportResponse();
            res.setEmployeeId(empId);
            res.setTotalHours(total);
            res.setEntries(entries);

            result.put(empId, res);
        }

        return result;
    }
}