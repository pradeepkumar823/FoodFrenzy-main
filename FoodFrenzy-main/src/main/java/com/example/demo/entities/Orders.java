package com.example.demo.entities;

import java.util.Date;
import java.util.Objects;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "orders_table")
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderId;
	@NotNull
	private String orderName;
	@NotNull
	private double orderPrice;
	private int orderQuantity;
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderDate;
	private double orderTotalAmount;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	// Constructor
	public Orders() {
	}

	public Orders(String orderName, double orderPrice, int orderQuantity, double orderTotalAmount, User user) {
		this.orderName = orderName;
		this.orderPrice = orderPrice;
		this.orderQuantity = orderQuantity;
		this.orderTotalAmount = orderTotalAmount;
		this.user = user;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public int getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public double getOrderTotalAmount() {
		return orderTotalAmount;
	}

	public void setOrderTotalAmount(double orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Orders orders = (Orders) o;
		return orderId == orders.orderId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId);
	}

	@Override
	public String toString() {
		return "Orders [orderId=" + orderId + ", orderName=" + orderName + ", orderPrice=" + orderPrice
				+ ", orderQuantity=" + orderQuantity
				+ ", orderDate=" + orderDate + ", orderTotalAmount=" + orderTotalAmount + ", user=" + user + "]";
	}

}