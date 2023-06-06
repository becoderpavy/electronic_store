package com.electronic.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.electronic.dto.PageableResponse;
import com.electronic.dto.ProductDto;
import com.electronic.entites.Category;
import com.electronic.entites.Product;
import com.electronic.exception.ResourceNotFoundException;
import com.electronic.helper.Helper;
import com.electronic.repository.CategoryRepository;
import com.electronic.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private ModelMapper mapper;

	@Value("${product.image.path}")
	private String productUploadPath;

	@Override
	public ProductDto create(ProductDto productDto) {

		Product product = mapper.map(productDto, Product.class);

		product.setProductId(UUID.randomUUID().toString());
		product.setAddedDate(new Date());

		return mapper.map(productRepo.save(product), ProductDto.class);
	}

	@Override
	public ProductDto update(ProductDto productDto, String productId) {

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not found"));

		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setDiscoutPrice(productDto.getDiscoutPrice());
		product.setQuantity(productDto.getQuantity());
		product.setLive(productDto.isLive());
		product.setStock(productDto.isStock());
		product.setProductImage(productDto.getProductImage());

		Product updateProduct = productRepo.save(product);

		return mapper.map(updateProduct, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProduct(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equals("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Product> page = productRepo.findAll(pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		return response;
	}

	@Override
	public ProductDto get(String productId) {
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
		return mapper.map(product, ProductDto.class);
	}

	@Override
	public void deleteProduct(String productId) {
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not found"));
		productRepo.delete(product);

		String fullPath = productUploadPath + product.getProductImage();
		Path path = Paths.get(fullPath);
		try {
			Files.delete(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public PageableResponse<ProductDto> searchByTitle(int pageNo, int pageSize, String sortBy, String sortDir,
			String title) {

		Sort sort = (sortDir.equals("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Product> page = productRepo.findByTitleContaining(pageable, title);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		return response;

	}

	@Override
	public PageableResponse<ProductDto> getAllLive(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equals("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Product> page = productRepo.findByLiveTrue(pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		return response;
	}

	@Override
	public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category id is invalid"));

		Product product = mapper.map(productDto, Product.class);

		product.setProductId(UUID.randomUUID().toString());
		product.setAddedDate(new Date());
		product.setCategory(category);
		return mapper.map(productRepo.save(product), ProductDto.class);
	}

	@Override
	public ProductDto updateCategory(String productId, String categoryId) {

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not found"));

		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category id is invalid"));

		product.setCategory(category);

		return mapper.map(productRepo.save(product), ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllOfCategory(int pageNo, int pageSize, String sortBy, String sortDir,
			String CategoryId) {
		Category category = categoryRepo.findById(CategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category id is invalid"));
		Sort sort = (sortDir.equals("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Product> page = productRepo.findByCategory(pageable, category);

		return Helper.getPageableResponse(page, ProductDto.class);
	}

}
