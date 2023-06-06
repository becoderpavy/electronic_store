package com.electronic.dto;

import java.util.ArrayList;
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
public class OrdersDto {

	private String orderId;

	// status- pending,delivered,dispatched
	private String orderStatus = "PENDING";

	// not-paid, paid
	private String paymentStatus = "NOT_PAID";

	private int orderAmount;

	private String billingAddress;

	private String billingPhone;

	private String billingName;

	private Date orderDate = new Date();

	private Date deliverdDate;

	// private UserDto user;

	private List<OrdersItemDto> orderItems;

}
