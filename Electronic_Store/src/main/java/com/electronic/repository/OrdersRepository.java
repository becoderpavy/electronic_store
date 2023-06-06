package com.electronic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electronic.entites.Orders;
import com.electronic.entites.User;

public interface OrdersRepository extends JpaRepository<Orders, String> {

	List<Orders> findByUser(User user);

}
