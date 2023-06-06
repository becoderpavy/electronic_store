package com.electronic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.electronic.dto.AddItemToCartRequest;
import com.electronic.dto.ApiResponseMessage;
import com.electronic.dto.CartDto;
import com.electronic.services.CartService;

@RestController
@RequestMapping("/carts")
public class CartControllers {

	@Autowired
	private CartService cartService;

	@PostMapping("/{userId}")
	public ResponseEntity<?> addItemToCart(@RequestBody AddItemToCartRequest request, @PathVariable String userId) {
		// System.out.println(request.getProductId());
		CartDto cartdto = cartService.addItemToCart(userId, request);
		//System.out.println(cartdto);
		return new ResponseEntity<>(cartdto, HttpStatus.OK);
	}

	@DeleteMapping("/{userId}/items/{itemId}")
	public ResponseEntity<?> removeItem(@PathVariable String userId, @PathVariable int itemId) {
		cartService.removeItemFromCart(userId, itemId);
		ApiResponseMessage msg = ApiResponseMessage.builder().message("item removed").build();
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> clearCart(@PathVariable String userId) {
		cartService.clearCart(userId);
		ApiResponseMessage msg = ApiResponseMessage.builder().message("item clear from cart").build();
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getCart(@PathVariable String userId) {
		return new ResponseEntity<>(cartService.getCartByUSer(userId), HttpStatus.OK);
	}

}
