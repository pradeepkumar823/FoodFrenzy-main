package com.example.demo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// import org.springframework.ui.Model; 
import com.example.demo.entities.User;
import com.example.demo.services.UserServices;

@Controller
@RequestMapping("/user")
public class UserController
{
	@Autowired
	private UserServices services;
			

	// //client based login(if using microservices)
	// @PostMapping("/userLogin")
	// public String userLogin(@ModelAttribute("userLoginForm") UserLoginDTO login, Model model) {
	// 	//use local service if client is not avilable
	// 	if(userClient == null){
	// 		boolean isValid=services.validationLoginCredentials(login.getEmail());
	// 		if(isValid){
	// 			User user=services.getUserByEmail(login.getEmail());
	// 			currentUserId=(long)user.getUserId();
	// 			return "redirect:/products";
	// 		}
	// 		else{
	// 			model.addAttribute("error2", "Invalid email or password");
	// 			return "Login";
	// 		}
	// 	}
		
		
	// 	//use client for microservice
	// 	try {
	// 		ApiResponse<Map<String, String>> response = userClient.login(login);
	// 		if (response != null && response.isSuccess()) {
	// 			currentUserToken = "Bearer " + response.getData().get("token");

	// 			// In a real app, decode JWT to get user ID
	// 			// For demo, set a dummy ID
	// 			currentUserId = 1L;
	// 			return "redirect:/products";
	// 		}
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}

	// 	model.addAttribute("error2", "Invalid email or password");
	// 	return "Login";
	// }


//entity based operation (for admin panel)
	@PostMapping("/addingUser")
	public String  addUser(@ModelAttribute User user){
		this.services.addUser(user);
		return "redirect:/admin/services";
	}
	@GetMapping("/allUser")
	public String allUsers(){
		return "allUsers";
	}

	@GetMapping("/updatingUser/{id}")
	public String updateUser(@ModelAttribute User user, @PathVariable("id") int id){
		this.services.updateUser(user, id);
		return "redirect:/admin/services";
	}

	@GetMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable("id" )int id){
		this.services.deleteUser(id);
		return "redirect:/admin/services";
	}
	


}
