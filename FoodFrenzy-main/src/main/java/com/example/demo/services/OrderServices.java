package com.example.demo.services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Orders;
import com.example.demo.entities.User;
import com.example.demo.repositories.OrderRepository;

@Component
@Service
public class OrderServices {
	@Autowired
	private OrderRepository orderRepository;

	public List<Orders> getOrders() {
		return this.orderRepository.findAll();

	}

	public void saveOrder(User user,Orders order) {
		order.setUser(user);
		this.orderRepository.save(order);
	}

	public void updateOrder(int id, Orders order) {
		order.setOrderId(id);
		this.orderRepository.save(order);

	}

	public List<Orders> getOrdersByUserId(int userId) {
		return this.orderRepository.findByUserUserId(userId);
	}

	// public void deleteOrder(int orderId) {
	// 	this.orderRepository.deleteById(orderId);
	// }

	public List<Orders> getOrdersForUser(User user){
		if(user==null){
			return List.of();
		}
		return this.orderRepository.findByUserUserId(user.getUserId());
	}


	public Orders getOrderById(int orderId){
		return this.orderRepository.findById(orderId).orElse(null);
	}

	

	
}