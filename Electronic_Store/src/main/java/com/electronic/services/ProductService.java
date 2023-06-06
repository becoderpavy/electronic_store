package com.electronic.services;

import java.util.List;

import com.electronic.dto.PageableResponse;
import com.electronic.dto.ProductDto;

public interface ProductService {

	ProductDto create(ProductDto productDto);

	ProductDto update(ProductDto productDto, String productId);

	PageableResponse<ProductDto> getAllProduct(int pageNo, int pageSize, String sortBy, String sortDir);

	ProductDto get(String productId);

	void deleteProduct(String productId);

	PageableResponse<ProductDto> searchByTitle(int pageNo, int pageSize, String sortBy, String sortDir, String title);

	PageableResponse<ProductDto> getAllLive(int pageNo, int pageSize, String sortBy, String sortDir);

	// create product with category
	ProductDto createWithCategory(ProductDto productDto, String categoryId);

	ProductDto updateCategory(String productId, String categoryId);

	PageableResponse<ProductDto> getAllOfCategory(int pageNo, int pageSize, String sortBy, String sortDir,
			String CategoryId);

}
