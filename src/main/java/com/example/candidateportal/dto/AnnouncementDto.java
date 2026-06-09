package com.example.candidateportal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.candidateportal.AnnouncementType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDto {

    private Long id;

    private String title;

    private String description;

    private AnnouncementType type;

    private LocalDate eventDate;

    private String createdBy;

    private LocalDateTime createdAt;
}