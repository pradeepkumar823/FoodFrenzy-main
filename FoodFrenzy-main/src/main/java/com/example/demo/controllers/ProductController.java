package com.example.demo.controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.*;
import com.example.demo.entities.*;
import com.example.demo.services.*;
import org.springframework.ui.Model;

@Controller
public class ProductController {
	@Autowired
	private ProductServices productServices;
	@Autowired
	private OrderServices orderServices;
	@Autowired
	private UserServices userServices;

	private Long currentUserId;
	private String currentUserToken;

	@GetMapping("/product")
	public String product(Model model) {
		return "Product";
	}

	// AddProduct
	@PostMapping("/product/addingProduct")
	public String addProduct(@ModelAttribute Product product) {

		this.productServices.addProduct(product);
		return "redirect:/admin/services";
	}

	// UpdateProduct
	@GetMapping("/updatingProduct/{productId}")
	public String updateProduct(@ModelAttribute Product product, @PathVariable("productId") int id) {

		this.productServices.updateProduct(product, id);
		return "redirect:/admin/services";
	}

	// DeleteProduct
	@GetMapping("/deleteProduct/{productId}")
	public String delete(@PathVariable("productId") int id) {
		this.productServices.deleteProduct(id);
		return "redirect:/admin/services";
	}

	@PostMapping("/product/search")
	public String searchHandler(@RequestParam("productName") String name, Model model) {
		try {
			// Use local service instead of client if not microservices
			Product product = productServices.getProductByName(name);

			if (product != null) {
				model.addAttribute("product", product);
				model.addAttribute("order", new OrderRequestDTO());

				// Get user orders if logged in
				if (currentUserId != null) {
					try {
						List<OrderDTO> orders = orderServices.getOrdersByUserId(currentUserId.intValue())
								.stream()
								.map(OrderDTO::fromEntity)
								.collect(Collectors.toList());
						model.addAttribute("orders", orders);
					} catch (Exception e) {
						System.out.println("Failed to fetch orders: " + e.getMessage());
					}
				}
				return "BuyProduct";
			} else {
				model.addAttribute("message", "SORRY...! Product Unavailable");
			}
		} catch (Exception e) {
			model.addAttribute("message", "SORRY...! Product Unavailable");
		}

		model.addAttribute("order", new OrderRequestDTO());
		return "BuyProduct";
	}

	@PostMapping("/product/order")
	public String orderHandler(@ModelAttribute OrderRequestDTO orderRequest, Model model) {
		if (currentUserId == null || currentUserToken == null) {
			return "redirect:/login";
		}
		try {
			Orders order = new Orders();
			order.setOrderName(orderRequest.getProductName());
			order.setOrderPrice(orderRequest.getPrice());
			order.setOrderQuantity(orderRequest.getQuantity());

			double totalAmount = orderRequest.getPrice() * orderRequest.getQuantity();
			order.setOrderTotalAmount(totalAmount);
			order.setOrderDate(new Date());

			User user = userServices.getUser(currentUserId.intValue());
			orderServices.saveOrder(user, order);

			model.addAttribute("amount", totalAmount);
			return "Order_success";
		} catch (Exception e) {
			model.addAttribute("error", "Failed to create order");
			return "BuyProduct";

		}
	}

	@GetMapping("/back")
	public String back(Model model) {
		if (currentUserId != null) {
			List<OrderDTO> orders = orderServices.getOrdersByUserId(currentUserId.intValue())
					.stream()
					.map(OrderDTO::fromEntity)
					.collect(Collectors.toList());
			model.addAttribute("orders", orders);
		}
		model.addAttribute("order", new OrderRequestDTO());
		return "BuyProduct";
	}

	public void setCurrentUser(Long userId, String token) {
		this.currentUserId = userId;
		this.currentUserToken = token;
	}

	@GetMapping("/addProduct")
	public String addProduct() {
		return "Add_Product";
	}

	@GetMapping("/updateProduct/{productId}")
	public String updateProduct(@PathVariable("productId") Long id, Model model) {
		model.addAttribute("productId", id);
		return "Update_Product";

	}

	@GetMapping("/updateUser/{userId}")
	public String updateUserPage(@PathVariable("userId") Long id, Model model) {
		model.addAttribute("userId", id);
		return "Update_User";
	}
}