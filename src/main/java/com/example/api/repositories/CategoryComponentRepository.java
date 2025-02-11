package com.example.api.repositories;

import com.example.api.domain.categorycomponents.CategoryComponents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryComponentRepository extends JpaRepository<CategoryComponents, Long> {

//    List<CategoryComponents> findByChildCategoryId(Long id);
//    List<CategoryComponents> findByParentCategoryId(Long id);

    List<CategoryComponents> findByParentCategoryIdAndChildCategoryEnabledTrue(Long id);

    List<CategoryComponents> findByChildCategoryIdAndChildCategoryEnabledTrue(Long id);
}
