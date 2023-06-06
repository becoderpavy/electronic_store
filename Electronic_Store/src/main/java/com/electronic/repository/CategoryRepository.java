package com.electronic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electronic.entites.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
