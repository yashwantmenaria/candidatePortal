package com.example.candidateportal.repository;

import java.util.List;

import com.example.candidateportal.dto.BirthdayDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BirthdayResponse {

    private List<BirthdayDto> todayBirthdays;

    private List<BirthdayDto> upcomingBirthdays;
}