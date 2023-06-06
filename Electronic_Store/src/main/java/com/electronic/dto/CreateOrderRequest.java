package com.electronic.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreateOrderRequest {

	private String orderStatus = "PENDING";

	private String paymentStatus = "NOT_PAID";

	private int orderAmount;

	@NotBlank(message = "address required")
	private String billingAddress;

	@NotBlank(message = "phone no required")
	private String billingPhone;

	@NotBlank(message = "name required")
	private String billingName;

	@NotBlank(message = "cart id required")
	private String cartId;

	@NotBlank(message = "user id required")
	private String userId;

}
