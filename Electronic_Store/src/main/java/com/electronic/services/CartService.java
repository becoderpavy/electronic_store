package com.electronic.services;

import com.electronic.dto.AddItemToCartRequest;
import com.electronic.dto.CartDto;

public interface CartService {

	// add item cart
	CartDto addItemToCart(String userId, AddItemToCartRequest request);

	// remove item from cart
	void removeItemFromCart(String userId, int cartItem);

	// remove all item from cart
	void clearCart(String userId);

	CartDto getCartByUSer(String userId);
}
