package com.example.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FoodFrenzyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodFrenzyApplication.class, args);
	}
}	

// ##📊 How It Works - Flow Diagram
// ```
// User Request
//     ↓
// Controller (Your Gateway/Frontend Service)
//     ↓
// Client (ProductClient/OrderClient/UserClient)
//     ↓
// HTTP Request → External Microservice
//     ↓
// Response (ApiResponse<DTO>)
//     ↓
// Controller processes response
//     ↓
// Return to Use
