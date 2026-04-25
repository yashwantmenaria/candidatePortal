package com.example.candidateportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.candidateportal.entity.Holiday;
import com.example.candidateportal.service.HolidayService;

@RestController
@RequestMapping("/holidays")
public class HolidayController {

    @Autowired
    private HolidayService service;

    // ✅ HR only
    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public Holiday addHoliday(@RequestBody Holiday holiday) {
        return service.addHoliday(holiday);
    }

    // ✅ Everyone (HR + Employee)
    @GetMapping
    public List<Holiday> getAll() {
        return service.getAllHolidays();
    }

    // ✅ HR only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public Holiday update(@PathVariable Long id, @RequestBody Holiday holiday) {
        return service.updateHoliday(id, holiday);
    }
}