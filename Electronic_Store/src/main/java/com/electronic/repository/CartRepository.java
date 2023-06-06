package com.electronic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electronic.entites.Cart;
import com.electronic.entites.User;

public interface CartRepository extends JpaRepository<Cart, String> {

	Optional<Cart> findByUser(User user);

}
