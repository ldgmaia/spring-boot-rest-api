package com.example.api.repositories;

import com.example.api.domain.categories.Category;
import com.example.api.domain.categories.CategoryListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Boolean existsByName(String name);

    Page<Category> findAllByEnabledTrue(Pageable pagination);

    List<CategoryListDTO> findAllByCategoryGroupId(Long categoryGroupId);

    Boolean existsByCategoryGroupId(Long categoryGroupId);

}
