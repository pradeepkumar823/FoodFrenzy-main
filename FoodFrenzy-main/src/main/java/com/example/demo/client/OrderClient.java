package com.example.demo.client;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderRequestDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "order-service", url = "${order.service.url}")
public interface OrderClient {

    
    @GetMapping("/api/orders")
    ApiResponse<List<OrderDTO>> getAllOrders();

    @GetMapping("/api/orders/{id}")
    ApiResponse<OrderDTO> getOrderById(@PathVariable("id") int orderId);

    @GetMapping("/api/orders/user/{userId}")
    ApiResponse<List<OrderDTO>> getOrdersByUserId(@PathVariable("userId") int userId);

    @PostMapping("/api/orders")
    ApiResponse<OrderDTO> createOrder(@RequestBody OrderRequestDTO orderRequest, @RequestHeader("Authorization") String token);



    @GetMapping("/api/order/{id}")
    ApiResponse<OrderDTO> deleteOrder(@PathVariable("id") int id);    

    

    

   
   
}