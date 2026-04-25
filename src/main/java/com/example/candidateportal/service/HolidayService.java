package com.example.candidateportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.candidateportal.entity.Holiday;
import com.example.candidateportal.repository.HolidayRepository;

@Service
public class HolidayService {

    @Autowired
    private HolidayRepository repo;

    public Holiday addHoliday(Holiday holiday) {
        return repo.save(holiday);
    }

    public List<Holiday> getAllHolidays() {
        return repo.findAll();
    }

    public Holiday updateHoliday(Long id, Holiday updated) {
        Holiday existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));

        existing.setTitle(updated.getTitle());
        existing.setDate(updated.getDate());
        existing.setDescription(updated.getDescription());
        existing.setDay(updated.getDay());
        return repo.save(existing);
    }
}