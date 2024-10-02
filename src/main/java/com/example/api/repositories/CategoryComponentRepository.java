package com.example.api.repositories;

import com.example.api.domain.categorycomponents.CategoryComponents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryComponentRepository extends JpaRepository<CategoryComponents, Long> {

    //    Page<CategoryGroup> findAllByEnabledTrue(Pageable pagination);
//
//    Boolean existsByName(String name);
//
//    @Query("""
//            select fg.enabled
//            from CategoryGroup fg
//            where fg.id = :categoryGroupId
//            """)

//    Boolean findEnabledById(Long categoryGroupId);

    @Query("""
            SELECT cc
            FROM CategoryComponent cc
            WHERE cc.enabled = true
            AND cc.childCategory.id = :childCategoryId
            """)
    List<CategoryComponents> findCategoryComponentByChildCategoryId(Long childCategoryId);

    @Query("""
            SELECT cc
            FROM CategoryComponent cc
            WHERE cc.enabled = true
            AND cc.parentCategory.id = :parentCategoryId
            """)
    List<CategoryComponents> findComponentsByParentCategoryId(Long parentCategoryId);

    List<CategoryComponents> findByChildCategoryId(Long id);
}
