package com.electronic.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electronic.dto.ApiResponseMessage;
import com.electronic.dto.CreateOrderRequest;
import com.electronic.dto.OrderStatusRequest;
import com.electronic.services.OrdersService;

@RestController
@RequestMapping("/orders")
public class OrdersController {

	@Autowired
	private OrdersService orderService;

	@PostMapping("/")
	public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderRequest request) {
		return new ResponseEntity<>(orderService.createOrder(request), HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{orderId}")
	public ResponseEntity<?> removeOrder(@PathVariable String orderId) {
		orderService.removeOrder(orderId);

		ApiResponseMessage message = ApiResponseMessage.builder().message("Order delete sucess").status(HttpStatus.OK)
				.success(true).build();

		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getOrderByUser(@PathVariable String userId) {
		return new ResponseEntity<>(orderService.getOrdersOfUser(userId), HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<?> getOrders(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "orderDate", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
		return new ResponseEntity<>(orderService.getOrders(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
	}

	@PutMapping("/")
	public ResponseEntity<?> updateStatus(@RequestBody OrderStatusRequest request) {
		return new ResponseEntity<>(orderService.updateStatus(request), HttpStatus.OK);
	}

}
