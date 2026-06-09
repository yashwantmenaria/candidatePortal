package com.example.candidateportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.candidateportal.entity.Employee;
import com.example.candidateportal.entity.Role;
import com.example.candidateportal.entity.User;
import com.example.candidateportal.repository.EmployeeRepository;
import com.example.candidateportal.repository.UserRepository;
import com.example.candidateportal.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private EmployeeRepository employeeRepo;

    public String login(String email, String password) {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        Employee employee = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Long empId = employee.getId();

        return jwtUtil.generateToken(user.getEmail(),empId, roles);
    }
    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }
}