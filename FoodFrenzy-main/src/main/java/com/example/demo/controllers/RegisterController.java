package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.demo.client.AdminClient;
import com.example.demo.client.UserClient;
import com.example.demo.dto.AdminDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entities.Admin;
import com.example.demo.entities.User;
import com.example.demo.services.AdminServices;
import com.example.demo.services.UserServices;
import com.example.demo.services.EmailService;

@Controller
public class RegisterController {

    @Autowired(required = false)
    private AdminClient adminClient;

    @Autowired(required = false)
    private UserClient userClient;

    @Autowired
    private UserServices userServices;

    @Autowired
    private AdminServices adminServices;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO userDto,
            @RequestParam("role") String role,
            @ModelAttribute("admin") AdminDTO adminDto,
            Model model,
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        try {
            if ("ADMIN".equalsIgnoreCase(role)) {
                if (adminClient != null) {
                    adminClient.createAdmin(adminDto);
                } else {
                    Admin admin = adminDto.toEntity();
                    adminServices.addAdmin(admin);
                    // Send verification email
                    emailService.sendVerificationEmail(adminDto.getEmail(), adminDto.getName(),
                            admin.getVerificationCode(), "ADMIN");
                }
                redirectAttributes.addFlashAttribute("success",
                        "Admin registered successfully! Please check your email to verify account.");
                return "redirect:/login";
            } else {
                if (userClient != null) {
                    userDto.setRole("USER");
                    userClient.registerUser(userDto);
                } else {
                    User user = userDto.toEntity();
                    userServices.addUser(user);
                    // Send verification email
                    emailService.sendVerificationEmail(userDto.getEmail(), userDto.getName(),
                            user.getVerificationCode(), "USER");
                }
                redirectAttributes.addFlashAttribute("success",
                        "User registered successfully! Please check your email to verify account.");
                return "redirect:/login";
            }
        } catch (

        Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            model.addAttribute("user", new UserDTO());
            model.addAttribute("admin", new AdminDTO());
            return "Register";
        }
    }

    @GetMapping("/admin/register")
    public String registerAdminPage(Model model) {
        model.addAttribute("admin", new AdminDTO());
        return "RegisterAdmin";
    }

    @PostMapping("/admin/register")
    public String registerAdmin(@ModelAttribute("admin") AdminDTO adminDTO, RedirectAttributes redirectAttributes) {
        try {
            if (adminClient != null) {
                adminClient.createAdmin(adminDTO);
                redirectAttributes.addFlashAttribute("success", "Admin registered successfully! Please login.");
            } else {
                Admin admin = adminDTO.toEntity();
                adminServices.addAdmin(admin);

                // Send verification email
                emailService.sendVerificationEmail(admin.getAdminEmail(), admin.getAdminName(),
                        admin.getVerificationCode(), "ADMIN");
            }
            redirectAttributes.addFlashAttribute("success",
                    "Admin registered successfully! Please check your email to verify account.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed");
            return "redirect:/admin/register";
        }
    }

}
