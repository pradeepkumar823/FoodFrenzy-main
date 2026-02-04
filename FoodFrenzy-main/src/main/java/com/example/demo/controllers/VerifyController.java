package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.services.AdminServices;
import com.example.demo.services.UserServices;

@Controller
public class VerifyController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private AdminServices adminServices;

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("code") String code, @RequestParam("role") String role, Model model) {
        boolean verified = false;

        if ("ADMIN".equalsIgnoreCase(role)) {
            verified = adminServices.verifyAdmin(code);
        } else {
            verified = userServices.verifyUser(code);
        }

        if (verified) {
            return "VerificationSuccess";
        } else {
            model.addAttribute("error", "Verification failed. Invalid or expired code.");
            return "VerificationFailed";
        }
    }
}
