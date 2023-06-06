package com.electronic.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersItemDto {

	private int orderItemId;

	private int quantity;

	private int totalPrice;

	private ProductDto product;

	// private OrdersDto orders;

}
