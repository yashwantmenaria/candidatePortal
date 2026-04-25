package com.example.candidateportal.response;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class MonthlyReportResponse {

    private Long employeeId;
    private Double totalHours;
    private List<DailyEntry> entries;

    // inner class
    @Data
    public static class DailyEntry {
        private LocalDate date;
        private Double hours;
        private String status;
    }
}