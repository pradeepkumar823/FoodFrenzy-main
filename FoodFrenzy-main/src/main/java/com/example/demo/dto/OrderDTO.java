package com.example.demo.dto;

import java.time.LocalDate;
import java.time.ZoneId;

import com.example.demo.entities.Orders;

public class OrderDTO {
    private Long orderId;
    private String productName;
    private Integer quantity;
    private Double totalAmount;
    private LocalDate orderDate;

public OrderDTO() {}

public static OrderDTO fromEntity(Orders order) {
        if (order == null) return null;
        
        OrderDTO dto = new OrderDTO();
        dto.setOrderId((long) order.getOrderId());
        dto.setProductName(order.getOrderName());
        dto.setQuantity(order.getOrderQuantity());
        dto.setTotalAmount(order.getOrderTotalAmount());
        if (order.getOrderDate() != null) {
            dto.setOrderDate(order.getOrderDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        return dto;
    }

    // Convert DTO to Entity
    public Orders toEntity() {
        Orders order = new Orders();
        if (this.orderId != null) {
            order.setOrderId(this.orderId.intValue());
        }
        order.setOrderName(this.productName);
        order.setOrderQuantity(this.quantity);
        order.setOrderTotalAmount(this.totalAmount);
        if (this.orderDate != null) {
            order.setOrderDate(java.util.Date.from(
                    this.orderDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        return order;
    }


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}
