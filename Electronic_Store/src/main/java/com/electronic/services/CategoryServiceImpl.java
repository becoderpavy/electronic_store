package com.electronic.services;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.electronic.dto.CategoryDto;
import com.electronic.dto.PageableResponse;
import com.electronic.entites.Category;
import com.electronic.exception.ResourceNotFoundException;
import com.electronic.helper.Helper;
import com.electronic.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CategoryDto create(CategoryDto categoryDto) {

		categoryDto.setId(UUID.randomUUID().toString());

		Category category = mapper.map(categoryDto, Category.class);
		return mapper.map(categoryRepo.save(category), CategoryDto.class);
	}

	@Override
	public CategoryDto update(CategoryDto categoryDto, String categoryId) {

		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category id is invalid"));

		category.setTitle(categoryDto.getTitle());
		category.setDescription(categoryDto.getDescription());
		category.setCoverImage(categoryDto.getCoverImage());
		Category updateCategory = categoryRepo.save(category);

		return mapper.map(updateCategory, CategoryDto.class);
	}

	@Override
	public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Category> page = categoryRepo.findAll(pageable);
		PageableResponse<CategoryDto> response = Helper.getPageableResponse(page, CategoryDto.class);

		return response;
	}

	@Override
	public void delete(String categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category id is invalid"));
		categoryRepo.delete(category);

	}

	@Override
	public CategoryDto get(String categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category id is invalid"));

		return mapper.map(category, CategoryDto.class);

	}

}
