package com.electronic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.electronic.entites.Category;
import com.electronic.entites.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

	Page<Product> findByTitleContaining(Pageable page, String title);

	Page<Product> findByLiveTrue(Pageable pageable);

	Page<Product> findByCategory(Pageable page, Category category);

}
