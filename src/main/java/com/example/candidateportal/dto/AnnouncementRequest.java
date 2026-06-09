package com.example.candidateportal.dto;

import java.time.LocalDate;

import com.example.candidateportal.AnnouncementType;

import lombok.Data;

@Data
public class AnnouncementRequest {

    private String title;

    private String description;

    private AnnouncementType type;

    private LocalDate eventDate;
}