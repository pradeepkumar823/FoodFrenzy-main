package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.services.AdminServices;
import com.example.demo.services.UserServices;

@Controller
public class VerifyController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private AdminServices adminServices;

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("code") String code,
            @RequestParam(value = "role", defaultValue = "USER") String role,
            RedirectAttributes redirectAttributes) {
        try {
            if ("ADMIN".equalsIgnoreCase(role)) {
                boolean verified = adminServices.verifyAdmin(code);
                if (verified) {
                    redirectAttributes.addFlashAttribute("success",
                            "Admin account verified successfully! You can now login.");
                } else {
                    redirectAttributes.addFlashAttribute("error",
                            "Invalid or expired verification link.");
                }
            } else {
                boolean verified = userServices.verifyUser(code);
                if (verified) {
                    redirectAttributes.addFlashAttribute("success",
                            "Email verified successfully! You can now login.");
                } else {
                    redirectAttributes.addFlashAttribute("error",
                            "Invalid or expired verification link.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",
                    "Verification failed. Please try again or contact support.");
        }
        return "redirect:/login";
    }
}
