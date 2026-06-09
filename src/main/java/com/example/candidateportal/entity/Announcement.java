package com.example.candidateportal.entity;

import java.time.LocalDate;

import com.example.candidateportal.AnnouncementType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "announcements")
public class Announcement extends BaseEntity {

	private String title;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Enumerated(EnumType.STRING)
	private AnnouncementType type;

	private LocalDate eventDate;

	private boolean active = true;

}