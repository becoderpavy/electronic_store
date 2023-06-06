package com.electronic.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.electronic.dto.ApiResponseMessage;
import com.electronic.dto.ImageResponse;
import com.electronic.dto.ProductDto;
import com.electronic.dto.UserDto;
import com.electronic.services.FileService;
import com.electronic.services.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private FileService fileService;

	@Value("${product.image.path}")
	private String productUploadPath;

	private Logger logger = LoggerFactory.getLogger(HomeController.class);

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto) {
		return new ResponseEntity<>(productService.create(productDto), HttpStatus.CREATED);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{productId}")
	public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable String productId) {
		return new ResponseEntity<>(productService.update(productDto, productId), HttpStatus.OK);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
		productService.deleteProduct(productId);
		ApiResponseMessage msg = ApiResponseMessage.builder().message("Category Delete Sucessfully").build();
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<?> getProduct(@PathVariable String productId) {
		return new ResponseEntity<>(productService.get(productId), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> getAllProduct(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		return new ResponseEntity<>(productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
	}

	@GetMapping("/search/{query}")
	public ResponseEntity<?> searchProduct(@PathVariable String query,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		return new ResponseEntity<>(productService.searchByTitle(pageNumber, pageSize, sortBy, sortDir, query),
				HttpStatus.OK);
	}

	@GetMapping("/live")
	public ResponseEntity<?> getAllLiveProduct(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		return new ResponseEntity<>(productService.getAllLive(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
	}

	
	@PostMapping("/image/{productId}")
	public ResponseEntity<ImageResponse> uploadProductImage(@PathVariable String productId,
			@RequestParam("prod_img") MultipartFile file) throws IOException {

		String imageName = fileService.uploadFile(file, productUploadPath);

		ProductDto productDto = productService.get(productId);
		productDto.setProductImage(imageName);
		ProductDto updateProduct = productService.update(productDto, productId);
		ImageResponse imgRes = ImageResponse.builder().imageName(updateProduct.getProductImage()).success(true)
				.status(HttpStatus.CREATED).message("image upload sucessfully").build();
		return new ResponseEntity<ImageResponse>(imgRes, HttpStatus.CREATED);

	}

	@GetMapping("/image/{productId}")
	public void serveProductImage(@PathVariable String productId, HttpServletResponse resp) throws IOException {
		ProductDto productDto = productService.get(productId);

		logger.info("Product image name :{}", productDto.getProductImage());
		InputStream ios = fileService.getFile(productUploadPath, productDto.getProductImage());

		resp.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(ios, resp.getOutputStream());
	}

}
