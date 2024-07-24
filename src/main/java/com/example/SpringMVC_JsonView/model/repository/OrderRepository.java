package com.example.SpringMVC_JsonView.model.repository;

import com.example.SpringMVC_JsonView.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
