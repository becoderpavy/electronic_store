package com.electronic.services;

import java.util.List;

import com.electronic.dto.CreateOrderRequest;
import com.electronic.dto.OrderStatusRequest;
import com.electronic.dto.OrdersDto;
import com.electronic.dto.PageableResponse;

public interface OrdersService {

	OrdersDto createOrder(CreateOrderRequest orderrequest);

	void removeOrder(String orderId);

	List<OrdersDto> getOrdersOfUser(String userId);

	PageableResponse<OrdersDto> getOrders(int pageNo, int pageSize, String sortBy, String sortDir);

	OrdersDto updateStatus(OrderStatusRequest request);
	
	
}
