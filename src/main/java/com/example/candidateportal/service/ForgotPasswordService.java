package com.example.candidateportal.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.candidateportal.entity.OtpEntity;
import com.example.candidateportal.entity.User;
import com.example.candidateportal.repository.OtpRepository;
import com.example.candidateportal.repository.UserRepository;
import com.example.candidateportal.utils.EmailService;

@Service
public class ForgotPasswordService {

	@Autowired
	private OtpRepository otpRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public String sendOtp(String email) {

	    String otp = String.valueOf(new Random().nextInt(900000) + 100000);

	    OtpEntity otpEntity = new OtpEntity();
	    otpEntity.setEmail(email);
	    otpEntity.setOtp(otp);
	    otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

	    otpRepository.save(otpEntity);

	    // Email send (simple)
	    emailService.sendEmail(email, "OTP for password reset", "Your OTP is: " + otp);

	    return "OTP sent to email";
	}
	
	public String verifyOtpAndChangePassword(String email, String otp, String newPassword) {

	    OtpEntity otpEntity = otpRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("OTP not found"));

	    // 1. OTP match
	    if (!otpEntity.getOtp().equals(otp)) {
	        throw new RuntimeException("Invalid OTP");
	    }

	    // 2. Expiry check
	    if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
	        throw new RuntimeException("OTP expired");
	    }

	    // 3. Change password
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    user.setPassword(passwordEncoder.encode(newPassword));
	    userRepository.save(user);

	    // 4. Delete OTP
	    otpRepository.delete(otpEntity);

	    return "Password reset successful";
	}
}
