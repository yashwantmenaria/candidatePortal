package com.example.candidateportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.candidateportal.dto.AnnouncementDto;
import com.example.candidateportal.dto.AnnouncementRequest;
import com.example.candidateportal.dto.BirthdayDto;
import com.example.candidateportal.dto.DashboardDto;
import com.example.candidateportal.dto.RecentEmployeeDto;
import com.example.candidateportal.entity.Announcement;
import com.example.candidateportal.entity.Employee;
import com.example.candidateportal.entity.User;
import com.example.candidateportal.repository.AnnouncementRepository;
import com.example.candidateportal.repository.BirthdayResponse;
import com.example.candidateportal.repository.EmployeeRepository;
import com.example.candidateportal.repository.UserRepository;

@Service
public class DashboardService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private AnnouncementRepository announcementRepository;
	
	public DashboardDto getDashboard() {

	    long totalEmployees = employeeRepository.count();

	    List<Employee> recentEmployees =
	            employeeRepository.findTop5ByOrderByJoiningDateDesc();

	    List<RecentEmployeeDto> recentList =
	            recentEmployees.stream()
	                    .map(emp -> RecentEmployeeDto.builder()
	                            .employeeId(emp.getEmployeeId())
	                            .name(emp.getFirstName() + " " + emp.getLastName())
	                            .email(emp.getEmail())
	                            .department(emp.getDepartment())
	                            .designation(emp.getDesignation())
	                            .joiningDate(emp.getJoiningDate())
	                            .build())
	                    .toList();

	    return DashboardDto.builder()
	            .totalEmployees(totalEmployees)
	            .recentEmployees(recentList)
	            .build();
	}
	
	public BirthdayResponse getBirthdays() {

	    List<Employee> today =
	            employeeRepository.findTodaysBirthdays();

	    List<Employee> upcoming =
	            employeeRepository.findUpcomingBirthdays();

	    if (upcoming.size() < 2) {
	        upcoming.addAll(
	            employeeRepository.findUpcomingBirthdaysFromStartOfYear()
	        );
	    }

	    return BirthdayResponse.builder()
	            .todayBirthdays(
	                    today.stream()
	                            .map(this::convertBirthdayDto)
	                            .toList())
	            .upcomingBirthdays(
	                    upcoming.stream()
	                            .distinct()
	                            .limit(2)
	                            .map(this::convertBirthdayDto)
	                            .toList())
	            .build();
	}

	private BirthdayDto convertBirthdayDto(Employee employee) {

	    return BirthdayDto.builder()
	            .employeeId(employee.getEmployeeId())
	            .name(employee.getFirstName() + " " + employee.getLastName())
	            .department(employee.getDepartment())
	            .email(employee.getEmail())
	            .dob(employee.getDob())
	            .build();
	}

	public AnnouncementDto createAnnouncement(
            AnnouncementRequest request) {

        Authentication auth =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = auth.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow();

        Announcement announcement =
                new Announcement();

        announcement.setTitle(request.getTitle());
        announcement.setDescription(request.getDescription());
        announcement.setType(request.getType());
        announcement.setEventDate(request.getEventDate());
        announcement.setActive(true);
        announcement.setCreatedBy(currentUser.getId().toString());

        announcement =
                announcementRepository.save(announcement);

        return convertToDto(announcement);
    }

    public List<AnnouncementDto> getAnnouncements() {

        return announcementRepository
                .findByActiveTrueOrderByCreatedAtDesc(
                        PageRequest.of(0, 10))
                .getContent()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    private AnnouncementDto convertToDto(
            Announcement announcement) {

        return AnnouncementDto.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .description(announcement.getDescription())
                .type(announcement.getType())
                .eventDate(announcement.getEventDate())
                .createdAt(announcement.getCreatedAt())
                .build();
    }
}
