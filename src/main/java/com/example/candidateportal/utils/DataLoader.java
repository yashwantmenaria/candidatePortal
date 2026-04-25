package com.example.candidateportal.utils;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.candidateportal.entity.Role;
import com.example.candidateportal.entity.User;
import com.example.candidateportal.repository.RoleRepository;
import com.example.candidateportal.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) {

        if (repo.count() == 0) {
            User admin = new User();
            admin.setEmail("admin@company.com");
            admin.setPassword(encoder.encode("admin123"));
            Role hrRole = roleRepo.findByName("HR")
                    .orElseThrow(() -> new RuntimeException("HR role not found"));

            Set<Role> roles = new HashSet<>();
            roles.add(hrRole);

            admin.setRoles(roles);
            repo.save(admin);


            System.out.println("Default HR created");
        }
    }
}