package com.example.demo.client;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ProductDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service", url = "${product.service.url}")
public interface ProductClient {

    @GetMapping("/api/products")
    ApiResponse<List<ProductDTO>>getAllProducts();

    @GetMapping("/api/products/{id}")
    ApiResponse<ProductDTO> getProductById(@PathVariable("id") int productId);

    @GetMapping("/api/products/search")
    ApiResponse<ProductDTO> getProductByName(@RequestParam("name") String productName);

    @PostMapping("/api/products")
    ApiResponse<ProductDTO> createProduct(@RequestBody ProductDTO product);

    @PutMapping("/api/products/{id}")
    ApiResponse<ProductDTO> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDTO product);

    @DeleteMapping("/api/products/{id}")
    ApiResponse<Void> deleteProduct(@PathVariable("id") int id);
}