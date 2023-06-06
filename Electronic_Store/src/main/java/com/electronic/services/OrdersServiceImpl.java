package com.electronic.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.electronic.dto.CreateOrderRequest;
import com.electronic.dto.OrderStatusRequest;
import com.electronic.dto.OrdersDto;
import com.electronic.dto.PageableResponse;
import com.electronic.entites.Cart;
import com.electronic.entites.CartItem;
import com.electronic.entites.OrderItems;
import com.electronic.entites.Orders;
import com.electronic.entites.User;
import com.electronic.exception.BadApiRequestException;
import com.electronic.exception.ResourceNotFoundException;
import com.electronic.helper.Helper;
import com.electronic.repository.CartRepository;
import com.electronic.repository.OrdersRepository;
import com.electronic.repository.ProductRepository;
import com.electronic.repository.UserRepository;

@Service
public class OrdersServiceImpl implements OrdersService {

	@Autowired
	private OrdersRepository orderRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CartRepository cartRepo;

	@Override
	public OrdersDto createOrder(CreateOrderRequest orderDto) {

		// Product product = productRepo.findById(productId)
		// .orElseThrow(() -> new ResourceNotFoundException("Product Not found"));

		User user = userRepo.findById(orderDto.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("user not found"));

		Cart cart = cartRepo.findById(orderDto.getCartId())
				.orElseThrow(() -> new ResourceNotFoundException("cart item is not found with user"));

		List<CartItem> cartItems = cart.getItems();
		System.out.println(orderDto.getCartId());
		System.out.println(cart.getItems().size());
		if (cartItems.size() <= 0) {
			throw new BadApiRequestException("ADD items in cart ");
		}

		// other checks

		Orders orders = Orders.builder().billingName(orderDto.getBillingName())
				.billingAddress(orderDto.getBillingAddress()).billingPhone(orderDto.getBillingPhone())
				.orderDate(new Date()).deliverdDate(null).paymentStatus(orderDto.getPaymentStatus())
				.orderStatus(orderDto.getOrderStatus()).orderId(UUID.randomUUID().toString()).user(user).build();

		// cartItem -> orderItem

		AtomicReference<Integer> orderAmount = new AtomicReference<Integer>(0);

		List<OrderItems> orderItems = cartItems.stream().map(c -> {

			OrderItems orderItem = OrderItems.builder().quantity(c.getQuantity()).product(c.getProduct())
					.totalPrice(c.getQuantity() * c.getProduct().getDiscoutPrice()).orders(orders).build();

			orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
			return orderItem;
		}).collect(Collectors.toList());

		orders.setOrderItems(orderItems);
		orders.setOrderAmount(orderAmount.get());

		cart.getItems().clear();
		cartRepo.save(cart);
		Orders saveOrders = orderRepo.save(orders);

		return mapper.map(saveOrders, OrdersDto.class);
	}

	@Override
	public void removeOrder(String orderId) {
		Orders orders = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order not found"));
		orderRepo.delete(orders);

	}

	@Override
	public List<OrdersDto> getOrdersOfUser(String userId) {

		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));

		List<Orders> orders = orderRepo.findByUser(user);

		return orders.stream().map(ord -> mapper.map(ord, OrdersDto.class)).collect(Collectors.toList());
	}

	@Override
	public PageableResponse<OrdersDto> getOrders(int pageNo, int pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equals("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<Orders> page = orderRepo.findAll(pageable);

		return Helper.getPageableResponse(page, OrdersDto.class);
	}

	@Override
	public OrdersDto updateStatus(OrderStatusRequest request) {

		Orders orders = orderRepo.findById(request.getOrderId())
				.orElseThrow(() -> new ResourceNotFoundException("order not found"));

		if ("PAID".equals(request.getPaymentStatus())) {
			orders.setOrderStatus(request.getOrderStatus());
			orders.setPaymentStatus("PAID");
			Orders updateOrders = orderRepo.save(orders);
			return mapper.map(updateOrders, OrdersDto.class);
		} else {
			throw new BadApiRequestException("user payment is failed");
		}

	}

}
