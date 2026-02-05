package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @org.springframework.beans.factory.annotation.Value("${APP_URL:http://localhost:8081}")
    private String appUrl;

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String name, String code, String role) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("pksingh76311@gmail.com");
        helper.setTo(to);
        helper.setSubject("FoodFrenzy - Account Verification");

        String verifyUrl = appUrl + "/verify?code=" + code + "&role=" + role;

        String content = "<h3>Hello, " + name + "!</h3>"
                + "<p>Thank you for registering with FoodFrenzy. Please click the link below to verify your account:</p>"
                + "<p><a href=\"" + verifyUrl + "\">Verify Account</a></p>"
                + "<br><p>Thank you,<br>FoodFrenzy Team</p>";

        helper.setText(content, true);

        mailSender.send(message);
    }
}
