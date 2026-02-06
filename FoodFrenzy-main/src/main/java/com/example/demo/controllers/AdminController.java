package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entities.*;
import com.example.demo.services.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserServices services;
	@Autowired
	private AdminServices adminServices;
	@Autowired
	private ProductServices productServices;
	@Autowired
	private OrderServices orderServices;

	@GetMapping("/services")
	public String returnBack(HttpSession session, Model model,
			org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
		if (!"ADMIN".equals(session.getAttribute("role"))) {
			redirectAttributes.addFlashAttribute("error", "Access Denied");
			return "redirect:/login";
		}
		List<User> users = this.services.getAllUser();
		List<Admin> admins = this.adminServices.getAll();
		List<Product> products = this.productServices.getAllProducts();
		List<Orders> orders = this.orderServices.getOrders();
		model.addAttribute("users", users);
		model.addAttribute("admins", admins);
		model.addAttribute("products", products);
		model.addAttribute("orders", orders);

		return "Admin_Page";
	}

	@GetMapping("/addAdmin")
	public String addAdminPage() {
		return "Add_Admin";
	}

	@PostMapping("/addingAdmin")
	public String addAdmin(@ModelAttribute Admin admin, RedirectAttributes ra) {
		try {
			this.adminServices.addAdmin(admin);
			ra.addFlashAttribute("success", "Admin added successfully!");
			return "redirect:/admin/services";
		} catch (Exception e) {
			ra.addFlashAttribute("error", "Failed to add admin: " + e.getMessage());
			return "redirect:/admin/addAdmin";
		}
	}

	@GetMapping("/updateAdmin/{adminId}")
	public String update(@PathVariable("adminId") int id, Model model) {
		Admin admin = this.adminServices.getAdmin(id);
		model.addAttribute("admin", admin);
		return "Update_Admin";
	}

	@PostMapping("/updatingAdmin/{id}")
	public String updateAdmin(@ModelAttribute Admin admin, @PathVariable("id") int id, RedirectAttributes ra) {
		try {
			this.adminServices.update(admin, id);
			ra.addFlashAttribute("success", "Admin updated successfully!");
			return "redirect:/admin/services";
		} catch (Exception e) {
			ra.addFlashAttribute("error", "Failed to update admin: " + e.getMessage());
			return "redirect:/admin/updateAdmin/" + id;
		}
	}

	@GetMapping("/deleteAdmin/{id}")
	public String deleteAdmin(@PathVariable("id") int id, RedirectAttributes ra) {
		this.adminServices.delete(id);
		ra.addFlashAttribute("success", "Admin deleted successfully!");
		return "redirect:/admin/services";
	}

	@GetMapping("/addProduct")
	public String addProduct() {
		return "Add_Product";
	}

	@GetMapping("/updateProduct/{productId}")
	public String updateProduct(@PathVariable("productId") int id, Model model) {
		Product product = this.productServices.getProduct(id);
		System.out.println(product);
		model.addAttribute("product", product);
		return "Update_Product";
	}

	@GetMapping("/addUser")
	public String addUser() {
		return "Add_User";
	}

	@GetMapping("/updateUser/{userId}")
	public String updateUserPage(@PathVariable("userId") int id, Model model) {
		User user = this.services.getUser(id);
		model.addAttribute("user", user);
		return "Update_User";
	}

	@GetMapping("/services/data")
	public String adminServices(HttpSession session, Model model,
			org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
		if (!"ADMIN".equals(session.getAttribute("role"))) {
			redirectAttributes.addFlashAttribute("error", "Access Denied");
			return "redirect:/login";
		}
		try {
			List<Product> products = productServices.getAllProducts();
			model.addAttribute("products", products);

			List<User> users = services.getAllUser();
			model.addAttribute("users", users);

			List<Admin> admins = adminServices.getAll();
			model.addAttribute("admins", admins);

			List<Orders> orders = orderServices.getOrders();
			model.addAttribute("orders", orders);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Admin_Page";
	}

}