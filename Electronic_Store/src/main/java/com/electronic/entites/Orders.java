package com.electronic.entites;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Orders {

	@Id
	private String orderId;

	// status- pending,delivered,dispatched
	private String orderStatus;

	// not-paid, paid
	private String paymentStatus;

	private int orderAmount;

	@Column(length = 4000)
	private String billingAddress;

	private String billingPhone;

	private String billingName;

	private Date orderDate;

	private Date deliverdDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "orders", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<OrderItems> orderItems = new ArrayList<OrderItems>();
}
