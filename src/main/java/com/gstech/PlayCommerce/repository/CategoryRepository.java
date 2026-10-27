package com.gstech.PlayCommerce.repository;

import com.gstech.PlayCommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
