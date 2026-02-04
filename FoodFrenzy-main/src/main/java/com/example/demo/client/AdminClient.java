package com.example.demo.client;

import com.example.demo.dto.AdminDTO;
import com.example.demo.dto.AdminLoginDTO;
import com.example.demo.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "admin-service" ,  url = "${admin.service.url}")
public interface AdminClient {

    @PostMapping("/api/admins/login")
    ApiResponse<Map<String,String>> adminLogin(@RequestBody AdminLoginDTO loginDTO);

    @GetMapping("/api/admins")
    ApiResponse<List<AdminDTO>> getAllAdmins();

    @GetMapping("api/admin/{id}")
    ApiResponse<AdminDTO> getAdminById(@PathVariable("id") Long id);

    @PostMapping("/api/admins")
    ApiResponse<AdminDTO> createAdmin(@RequestBody AdminDTO adminDTO);

    @PutMapping("/api/admins/{id}")
    ApiResponse<AdminDTO> updateAdmin(
            @PathVariable("id") Long id,
            @RequestBody AdminDTO adminDTO);

    @DeleteMapping("/api/admins/{id}")
    ApiResponse<Void> deleteAdmin(@PathVariable("id") Long id);
}