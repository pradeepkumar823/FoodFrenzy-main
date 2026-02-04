package com.example.demo.client;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserLoginDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {
    
    @PostMapping("/api/users/login")
    ApiResponse<Map<String, String>> login(@RequestBody UserLoginDTO loginDTO);

    @GetMapping("/api/users/{userId}")
    ApiResponse<UserDTO> getUserById(@PathVariable("userId") Long userId, @RequestHeader("Authorization") String token);

    @GetMapping("/api/users")
    ApiResponse<List<UserDTO>> getAllUsers();

    @PostMapping("/api/users/register")
    ApiResponse<UserDTO> registerUser(@RequestBody UserDTO userDTO);

    @PutMapping("/api/users/{id}")
    ApiResponse<UserDTO> updateUser(@PathVariable("id") Long id, @RequestBody UserDTO userDTO);

    @DeleteMapping("/api/users/{id}")
    ApiResponse<Void> deleteUser(@PathVariable("id") Long id);
}