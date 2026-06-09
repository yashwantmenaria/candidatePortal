package com.example.candidateportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.candidateportal.dto.AnnouncementDto;
import com.example.candidateportal.dto.AnnouncementRequest;
import com.example.candidateportal.dto.DashboardDto;
import com.example.candidateportal.repository.BirthdayResponse;
import com.example.candidateportal.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@GetMapping("/count")
	public ResponseEntity<DashboardDto> getDashboard() {

		return ResponseEntity.ok(dashboardService.getDashboard());
	}

	@GetMapping("/birthdays")
	public ResponseEntity<BirthdayResponse> getBirthdays() {

		return ResponseEntity.ok(dashboardService.getBirthdays());
	}

	@PostMapping("/announcements")
    @PreAuthorize("hasAnyRole('HR')")
	public ResponseEntity<AnnouncementDto> createAnnouncement(@RequestBody AnnouncementRequest request) {
		return ResponseEntity.ok(dashboardService.createAnnouncement(request));
	}
	
	@GetMapping
    public ResponseEntity<List<AnnouncementDto>>
            getAnnouncements() {

        return ResponseEntity.ok(
        		dashboardService.getAnnouncements());
    }
}
