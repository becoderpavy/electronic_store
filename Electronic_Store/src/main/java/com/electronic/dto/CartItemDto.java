package com.electronic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDto {
	private int cartItemId;

	private ProductDto product;

	private int quantity;

	private int totalPrice;
}
