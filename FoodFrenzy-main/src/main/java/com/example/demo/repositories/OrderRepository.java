package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer>{
	List<Orders> findByUserUserId(int userId);
}