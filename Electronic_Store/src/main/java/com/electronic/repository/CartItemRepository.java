package com.electronic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electronic.entites.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
