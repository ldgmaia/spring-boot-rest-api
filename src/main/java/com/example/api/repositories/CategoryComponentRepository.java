package com.example.api.repositories;

import com.example.api.domain.categorycomponents.CategoryComponents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryComponentRepository extends JpaRepository<CategoryComponents, Long> {

//    List<CategoryComponents> findByChildCategoryId(Long id);
//    List<CategoryComponents> findByParentCategoryId(Long id);
    
    List<CategoryComponents> findByParentCategoryIdAndChildCategoryEnabledTrue(Long id);

    List<CategoryComponents> findByChildCategoryIdAndChildCategoryEnabledTrue(Long id);
}
