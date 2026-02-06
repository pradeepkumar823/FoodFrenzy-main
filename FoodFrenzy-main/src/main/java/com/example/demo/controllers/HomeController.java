package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.*;
import com.example.demo.dto.OrderDTO;
import java.util.stream.Collectors;
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

	@Autowired
	private OrderServices orderServices;

	// for home page
	@GetMapping({ "/", "/home" })
	public String home(HttpSession session, Model model) {
		Object loggedInUser = session.getAttribute("loggedInUser");
		if (loggedInUser != null) {
			// in every single requset,it will check if the user is logged in or not
			// Spring automatically found the user from the Session/Cookie!
			model.addAttribute("user", loggedInUser);
			// We can get the role from the User object directly
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

		// First check if admin email exists
		if (!adminServices.validateAdminEmail(adminEmail)) {
			model.addAttribute("error", "Invalid email or password");
			return "Login";
		}

		// Then check if verified
		if (!adminServices.isAdminVerified(adminEmail)) {
			model.addAttribute("error", "Your account is not verified. Please click 'Verify with Google' first.");
			return "Login";
		}

		// Finally validate password
		if (adminServices.validateAdminCredentials(adminEmail, adminPassword)) {
			Admin admin = adminServices.getAll().stream()
					.filter(a -> a.getAdminEmail().equalsIgnoreCase(adminEmail))
					.findFirst().orElse(null);
			session.setAttribute("loggedInUser", admin);
			session.setAttribute("role", "ADMIN");

			// Integrate with Spring Security
			// UsernamePasswordAuthenticationToken: This is like an internal ID card that
			// says "This is Admin Pradeep and he has ADMIN powers."
			Authentication auth = new UsernamePasswordAuthenticationToken(admin, null,
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(auth);
			// SecurityContextHolder: This is the global "whiteboard" for the server. It
			// writes down who is currently logged in so every other part of the code can
			// see it.
			SecurityContextHolder.setContext(context);
			// Creates the actual Cookie
			// Storage: The browser stores the JSESSIONID in its memory.
			// Recognition: Every single time the user clicks a link (like "Profile" or
			// "Order"), the browser automatically sends that JSESSIONID back to the server.
			// Validation: The server looks at the ID, checks its memory, and says: "Oh,
			// this is Pradeep's phone, and he is correctly logged in!"
			securityContextRepository.saveContext(context, request, response);

			redirectAttributes.addFlashAttribute("success", "Admin logged in successfully!");
			return "redirect:/profile";
		} else {
			model.addAttribute("error", "Invalid email or password");
			return "Login";
		}

	}

	// 1. @Valid
	// The Guard: This tells Spring to check the "rules" of the login form
	// immediately. For example, if you set a rule that an email must not be empty,
	// @Valid will check that before the code even runs.
	// 2. @ModelAttribute("userLogin") UserLogin login
	// The Translator: This takes the data the user typed into the HTML form (Email
	// and Password) and "translates" it into a Java object called
	// login
	// .
	// Instead of you saying request.getParameter("email"), Spring does the work for
	// you and puts everything inside the
	// login
	// helper.
	// 3. Model model
	// The Delivery Truck: This is used to send data back to the HTML page.
	// If the login fails, you use model.addAttribute("error", "...") to "drive"
	// that error message back to the user's screen.
	// 4. HttpSession session
	// The Short-Term Memory: As we discussed, this is the server's way of
	// remembering who is currently at the computer.
	// Once the login is successful, you save the user here so they don't have to
	// log in again on the next page.
	// 5. HttpServletRequest request
	// The Incoming Letter: This is the "raw" data coming from the user's browser.
	// It contains everything: their IP address, the browser they are using
	// (Chrome/Safari), and the "Letter" (request) they sent to the server.
	// 6. HttpServletResponse response
	// The Outgoing Package: This is the "container" for the data you send back to
	// the user.
	// When you create a Cookie, you "put" it into this response so the browser can
	// catch it and store it.
	// 7. RedirectAttributes redirectAttributes
	// The "Flash" Messenger: This is a special tool for Redirects.
	// In Java, when you say return "redirect:/home", the Model data usually
	// disappears. RedirectAttributes allows an error or success message to
	// "survive" the redirect so it can be shown on the next page.
	// Think of it as a sticky note that stays on the user's screen even after they
	// move to a new room.

	@PostMapping("/userLogin")
	public String userLogin(@Valid @ModelAttribute("userLogin") UserLogin login, Model model,
			HttpSession session, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		String userEmail = login.getUserEmail();
		String userPassword = login.getUserPassword();

		// First check if user email exists
		User user = services.getUserByEmail(userEmail);
		if (user == null) {
			model.addAttribute("error", "Invalid email or password");
			return "Login";
		}

		// Finally validate password
		if (services.validateLoginCredentials(userEmail, userPassword)) {
			// Puts user info in the Session
			session.setAttribute("loggedInUser", user);
			session.setAttribute("role", "USER");

			// Integrate with Spring Security
			Authentication auth = new UsernamePasswordAuthenticationToken(user, null,
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(auth);
			SecurityContextHolder.setContext(context);
			// Creates the actual Cookie
			// Storage: The browser stores the JSESSIONID in its memory.
			// Recognition: Every single time the user clicks a link (like "Profile" or
			// "Order"), the browser automatically sends that JSESSIONID back to the server.
			// Validation: The server looks at the ID, checks its memory, and says: "Oh,
			// this is Pradeep's phone, and he is correctly logged in!"
			securityContextRepository.saveContext(context, request, response);

			redirectAttributes.addFlashAttribute("success", "User logged in successfully!");
			return "redirect:/profile";
		} else {
			model.addAttribute("error", "Invalid email or password");
			return "Login";
		}
	}

	@GetMapping("/profile")
	public String profile(HttpSession session, Model model) {
		Object loggedInUser = session.getAttribute("loggedInUser");
		model.addAttribute("user", loggedInUser);
		model.addAttribute("role", session.getAttribute("role"));

		if (loggedInUser != null && "USER".equals(session.getAttribute("role"))) {
			User user = (User) loggedInUser;
			List<OrderDTO> orders = orderServices.getOrdersByUserId(user.getUserId())
					.stream()
					.map(OrderDTO::fromEntity)
					.collect(Collectors.toList());
			model.addAttribute("orders", orders);
		}
		return "Profile";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/home";
	}

}