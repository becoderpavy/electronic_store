package com.electronic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electronic.entites.OrderItems;

public interface OrdersItemRepository extends JpaRepository<OrderItems, String> {

}
