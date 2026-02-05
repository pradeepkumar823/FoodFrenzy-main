package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.*;
import com.example.demo.loginCredentials.*;
import com.example.demo.entities.*;
import jakarta.servlet.http.HttpSession;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.AdminDTO;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {
	@Autowired
	private UserServices services;
	@Autowired
	private AdminServices adminServices;
	@Autowired
	private ProductServices productServices;

	@Autowired
	private SecurityContextRepository securityContextRepository;

	// for home page
	@GetMapping({ "/", "/home" })
	public String home(HttpSession session, Model model) {
		Object loggedInUser = session.getAttribute("loggedInUser");
		if (loggedInUser != null) {
			model.addAttribute("user", loggedInUser);
			model.addAttribute("role", session.getAttribute("role"));
		}
		return "Home";
	}

	// for product page
	@GetMapping("/products")
	public String products(HttpSession session, Model model) {
		List<Product> products = productServices.getAllProducts();
		model.addAttribute("products", products);
		Object loggedInUser = session.getAttribute("loggedInUser");
		if (loggedInUser != null) {
			model.addAttribute("user", loggedInUser);
			model.addAttribute("role", session.getAttribute("role"));
		}
		return "Products";
	}

	// for location page
	@GetMapping("/location")
	public String location(HttpSession session, Model model) {
		Object loggedInUser = session.getAttribute("loggedInUser");
		if (loggedInUser != null) {
			model.addAttribute("user", loggedInUser);
			model.addAttribute("role", session.getAttribute("role"));
		}
		return "Locate_us";
	}

	// for about page
	@GetMapping("/about")
	public String about(HttpSession session, Model model) {
		Object loggedInUser = session.getAttribute("loggedInUser");
		if (loggedInUser != null) {
			model.addAttribute("user", loggedInUser);
			model.addAttribute("role", session.getAttribute("role"));
		}
		return "About";
	}

	@GetMapping("/login")
	public String login(@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "success", required = false) String success,
			@RequestParam(value = "error", required = false) String error,
			HttpSession session,
			Model model) {
		if (session.getAttribute("loggedInUser") != null) {
			return "redirect:/profile";
		}
		com.example.demo.loginCredentials.AdminLogin adminLogin = new com.example.demo.loginCredentials.AdminLogin();
		com.example.demo.loginCredentials.UserLogin userLogin = new com.example.demo.loginCredentials.UserLogin();

		if (email != null) {
			adminLogin.setAdminEmail(email);
			userLogin.setUserEmail(email);
		}
		if (success != null) {
			model.addAttribute("success", success);
		}
		if (error != null) {
			model.addAttribute("error", error);
		}

		model.addAttribute("adminLogin", adminLogin);
		model.addAttribute("userLogin", userLogin);
		return "Login";
	}

	@GetMapping("/adminLogin")
	public String adminLogin(Model model) {
		return "AdminLogin";
	}

	// for registration page
	@GetMapping("/register")
	public String register(HttpSession session, Model model) {
		if (session.getAttribute("loggedInUser") != null) {
			return "redirect:/profile";
		}
		model.addAttribute("user", new UserDTO());
		model.addAttribute("admin", new AdminDTO());
		return "Register";
	}

	@PostMapping("/adminLogin")
	public String getAllData(@Valid @ModelAttribute("adminLogin") AdminLogin login, Model model,
			HttpSession session, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		String adminEmail = login.getAdminEmail();
		String adminPassword = login.getAdminPassword();
		if (adminServices.validateAdminCredentials(adminEmail, adminPassword)) {
			Admin admin = adminServices.getAll().stream()
					.filter(a -> a.getAdminEmail().equals(adminEmail))
					.findFirst().orElse(null);
			session.setAttribute("loggedInUser", admin);
			session.setAttribute("role", "ADMIN");

			// Integrate with Spring Security
			Authentication auth = new UsernamePasswordAuthenticationToken(admin, null,
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(auth);
			SecurityContextHolder.setContext(context);
			securityContextRepository.saveContext(context, request, response);

			redirectAttributes.addFlashAttribute("success", "Admin logged in successfully!");
			return "redirect:/profile";
		} else {
			model.addAttribute("error", "Invalid email or password");
			return "Login";
		}

	}

	@PostMapping("/userLogin")
	public String userLogin(@ModelAttribute("userLogin") UserLogin login, Model model,
			HttpSession session, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		String email = login.getUserEmail();
		String password = login.getUserPassword();

		if (services.validateLoginCredentials(email, password)) {
			User loggedInUser = this.services.getUserByEmail(email);
			session.setAttribute("loggedInUser", loggedInUser);
			session.setAttribute("role", "USER");

			// Integrate with Spring Security
			Authentication auth = new UsernamePasswordAuthenticationToken(loggedInUser, null,
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(auth);
			SecurityContextHolder.setContext(context);
			securityContextRepository.saveContext(context, request, response);

			redirectAttributes.addFlashAttribute("success", "User logged in successfully!");
			return "redirect:/profile";
		} else {
			model.addAttribute("error2", "Invalid email or password");
			return "Login";
		}

	}

	@GetMapping("/profile")
	public String profile(HttpSession session, Model model) {
		Object loggedInUser = session.getAttribute("loggedInUser");
		model.addAttribute("user", loggedInUser);
		model.addAttribute("role", session.getAttribute("role"));
		return "Profile";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/home";
	}

}