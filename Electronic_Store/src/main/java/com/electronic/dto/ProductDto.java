package com.electronic.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto {

	private String productId;

	private String title;

	private String description;

	private int price;

	private int discoutPrice;

	private int quantity;

	private Date addedDate;

	private boolean live;

	private boolean stock;

	private String productImage;

	private CategoryDto category;

}
