package com.electronic.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronic.dto.AddItemToCartRequest;
import com.electronic.dto.CartDto;
import com.electronic.entites.Cart;
import com.electronic.entites.CartItem;
import com.electronic.entites.Product;
import com.electronic.entites.User;
import com.electronic.exception.BadApiRequestException;
import com.electronic.exception.ResourceNotFoundException;
import com.electronic.repository.CartItemRepository;
import com.electronic.repository.CartRepository;
import com.electronic.repository.ProductRepository;
import com.electronic.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CartItemRepository cartItemRepo;

	@Override
	public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
		// add item to cart :
		// case1- cart for user is not available : we will create the cart and add
		// cartitem
		// case-2 cart available add the item to cart

		int quantity = request.getQuantity();
		String productId = request.getProductId();

		if (quantity <= 0) {
			throw new BadApiRequestException("Requested quantity is invalid");
		}

		// fetch product
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
		// fetch user
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));

		Cart cart = null;

		try {
			cart = cartRepo.findByUser(user).get();
		} catch (NoSuchElementException e) {
			cart = new Cart();
			cart.setCartId(UUID.randomUUID().toString());
			cart.setCreatedAt(new Date());

			e.printStackTrace();
		}

		// perform cart operation
		// if cart alreday present then update
		// boolean updated = false;
		AtomicReference<Boolean> updated = new AtomicReference<Boolean>(false);

		List<CartItem> items = cart.getItems();

		items = items.stream().map(item -> {
			if (item.getProduct().getProductId().equals(productId)) {
				// item already present in cart
				item.setQuantity(quantity);
				item.setTotalPrice(quantity * product.getDiscoutPrice());
				updated.set(true);
			}

			return item;
		}).collect(Collectors.toList());

		//cart.setItems(updatedItems);

		// create items
		// if(!updated)

		if (!updated.get()) {
			CartItem cartItem = CartItem.builder().quantity(quantity).totalPrice(quantity * product.getDiscoutPrice())
					.cart(cart).product(product).build();
			cart.getItems().add(cartItem);
		}

		cart.setUser(user);
		Cart updateCart = cartRepo.save(cart);

		return mapper.map(updateCart, CartDto.class);
	}

	@Override
	public void removeItemFromCart(String userId, int cartItem) {

		CartItem citem = cartItemRepo.findById(cartItem)
				.orElseThrow(() -> new ResourceNotFoundException("cart item is inavalid"));
		cartItemRepo.delete(citem);
	}

	@Override
	public void clearCart(String userId) {

		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
		Cart cart = cartRepo.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("cart item is not found with user"));
		cart.getItems().clear();
		cartRepo.save(cart);

	}

	@Override
	public CartDto getCartByUSer(String userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
		Cart cart = cartRepo.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("cart item is not found with user"));
		return mapper.map(cart, CartDto.class);
	}

}
