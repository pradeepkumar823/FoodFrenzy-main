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
import com.example.demo.services.EmailService;
import com.example.demo.services.UserServices;
import com.example.demo.utils.EmailNormalizer;

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
            // Consolidate and normalize email based on role
            String rawEmail = "ADMIN".equalsIgnoreCase(role) ? adminDto.getEmail() : userDto.getEmail();

            if (rawEmail == null || rawEmail.isEmpty()) {
                model.addAttribute("error", "Email is required");
                return "Register";
            }

            String normalizedEmail = EmailNormalizer.normalize(rawEmail);

            // Set normalized email back to DTOs for consistent registration
            if ("ADMIN".equalsIgnoreCase(role)) {
                adminDto.setEmail(normalizedEmail);
            } else {
                userDto.setEmail(normalizedEmail);
            }

            // Check for duplicate email in User table
            if (userServices.getUserByEmail(normalizedEmail) != null) {
                model.addAttribute("error", "Email already registered as a Customer");
                return "Register";
            }
            // Check for duplicate email in Admin table
            if (adminServices.validateAdminEmail(normalizedEmail)) {
                model.addAttribute("error", "Email already registered as an Admin");
                return "Register";
            }

            if ("ADMIN".equalsIgnoreCase(role)) {
                if (adminClient != null) {
                    adminClient.createAdmin(adminDto);
                } else {
                    Admin admin = adminDto.toEntity();
                    adminServices.addAdmin(admin);
                }
                redirectAttributes.addFlashAttribute("success",
                        "Admin registered successfully! Please click 'Verify with Google' on the login page to activate your account.");
                return "redirect:/login";
            } else {
                User user;
                if (userClient != null) {
                    userDto.setRole("USER");
                    userClient.registerUser(userDto);
                    user = userDto.toEntity();
                } else {
                    user = userDto.toEntity();
                    userServices.addUser(user);
                }

                // Send verification email - retrieve user from DB (lookup by normalized email)
                try {
                    User savedUser = userServices.getUserByEmail(normalizedEmail);
                    if (savedUser != null && savedUser.getVerificationCode() != null) {
                        emailService.sendVerificationEmail(
                                savedUser.getUserEmail(),
                                savedUser.getUserName(),
                                savedUser.getVerificationCode(),
                                "USER");
                        redirectAttributes.addFlashAttribute("success",
                                "Registration successful! Please check your email to verify your account.");
                    } else {
                        redirectAttributes.addFlashAttribute("success",
                                "Registration successful! However, we couldn't send the verification email. Please contact support.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("success",
                            "Registration successful! However, we couldn't send the verification email. Please contact support.");
                }
                return "redirect:/login";
            }
        } catch (Exception e) {
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
            String normalizedEmail = EmailNormalizer.normalize(adminDTO.getEmail());
            adminDTO.setEmail(normalizedEmail);

            // Check for duplicate email in User table
            if (userServices.getUserByEmail(normalizedEmail) != null) {
                redirectAttributes.addFlashAttribute("error", "Email already registered as a User");
                return "redirect:/admin/register";
            }
            // Check for duplicate email in Admin table
            if (adminServices.validateAdminEmail(normalizedEmail)) {
                redirectAttributes.addFlashAttribute("error", "Email already registered as an Admin");
                return "redirect:/admin/register";
            }

            if (adminClient != null) {
                adminClient.createAdmin(adminDTO);
                redirectAttributes.addFlashAttribute("success", "Admin registered successfully! Please login.");
            } else {
                Admin admin = adminDTO.toEntity();
                adminServices.addAdmin(admin);
            }
            redirectAttributes.addFlashAttribute("success",
                    "Admin registered successfully! Please click 'Verify with Google' on the login page to activate your account.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed");
            return "redirect:/admin/register";
        }
    }

}
