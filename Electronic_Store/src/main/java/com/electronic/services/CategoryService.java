package com.electronic.services;

import com.electronic.dto.CategoryDto;
import com.electronic.dto.PageableResponse;

public interface CategoryService {

	CategoryDto create(CategoryDto categoryDto);

	CategoryDto update(CategoryDto categoryDto, String categoryId);

	PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

	void delete(String categoryId);

	CategoryDto get(String categoryId);

}
