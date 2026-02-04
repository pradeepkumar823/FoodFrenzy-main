package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.entities.Orders;
import com.example.demo.services.OrderServices;


@Controller
public class OrderController {
    @Autowired
    private OrderServices orderServices;

    @GetMapping("/order/{id}")
    public String order(@PathVariable int id, Model model) {
        Orders order = orderServices.getOrderById(id);
        model.addAttribute("order", order);
        return "order";
    }

}
