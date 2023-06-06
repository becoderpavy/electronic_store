package com.electronic.entites;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

	@Id
	private String productId;

	private String title;

	@Column(length = 10000)
	private String description;

	private int price;

	private int discoutPrice;

	private int quantity;

	private Date addedDate;

	private boolean live;

	private boolean stock;

	private String productImage;

	@ManyToOne(fetch = FetchType.EAGER)
	// @JoinColumn(name = "category_id")
	private Category category;

}
