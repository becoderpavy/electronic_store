package com.electronic.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.electronic.entites.CartItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartDto {

	private String cartId;

	private Date createdAt;

	private UserDto user;

	private List<CartItemDto> items = new ArrayList<>();

}
