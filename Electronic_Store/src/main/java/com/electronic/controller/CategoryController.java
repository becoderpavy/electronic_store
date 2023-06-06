package com.electronic.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electronic.dto.ApiResponseMessage;
import com.electronic.dto.CategoryDto;
import com.electronic.dto.ProductDto;
import com.electronic.services.CategoryService;
import com.electronic.services.ProductService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody CategoryDto categoryDto) {
		return new ResponseEntity<>(categoryService.create(categoryDto), HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{categoryId}")
	public ResponseEntity<?> update(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
		return new ResponseEntity<>(categoryService.update(categoryDto, categoryId), HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<?> getAllCategory(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		return new ResponseEntity<>(categoryService.getAll(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<?> getCategory(@PathVariable String categoryId) {
		return new ResponseEntity<>(categoryService.get(categoryId), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
		categoryService.delete(categoryId);
		ApiResponseMessage msg = ApiResponseMessage.builder().message("Category Delete Sucessfully").build();
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	// create product with category
	@PostMapping("/{categoryId}/products")
	public ResponseEntity<?> createProductWithCategory(@PathVariable String categoryId,
			@RequestBody ProductDto productDto) {
		return new ResponseEntity<>(productService.createWithCategory(productDto, categoryId), HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{categoryId}/products/{productId}")
	public ResponseEntity<?> updateProductWithCategory(@PathVariable String categoryId,
			@PathVariable String productId) {
		return new ResponseEntity<>(productService.updateCategory(productId, categoryId), HttpStatus.OK);
	}

	@GetMapping("/{categoryId}/products")
	public ResponseEntity<?> getCategoryByProducts(@PathVariable String categoryId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		return new ResponseEntity<>(
				productService.getAllOfCategory(pageNumber, pageSize, sortBy, sortDir, categoryId),
				HttpStatus.OK);
	}

}
