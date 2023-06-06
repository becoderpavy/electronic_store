package com.electronic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusRequest {

	private String orderId;

	private String paymentStatus;

	private String orderStatus;

}
