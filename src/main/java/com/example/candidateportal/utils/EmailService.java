package com.example.candidateportal.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOnboardingMail(String to, String name, String empId, String tempPass) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Welcome to Company");

        message.setText(
                "Hi " + name + ",\n\n" +
                "Welcome to the company!\n\n" +
                "Employee ID: " + empId + "\n" +
                "Email: " + to + "\n" +
                "Temporary Password: " + tempPass + "\n\n" +
                "Please change your password after login.\n\n" +
                "Regards,\nHR"
        );

        mailSender.send(message);
    }

    public void sendEmail(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

	public void sendMailForGatePass(String managerEmail, String subject, String body) {
		   SimpleMailMessage message = new SimpleMailMessage();

	        message.setTo(managerEmail);
	        message.setSubject(subject);

	        message.setText(body);

	        mailSender.send(message);
		
	}
}