package com.example.candidateportal.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardDto {

    private Long totalEmployees;

    private List<RecentEmployeeDto> recentEmployees;
}