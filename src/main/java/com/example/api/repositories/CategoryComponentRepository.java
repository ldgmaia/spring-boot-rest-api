package com.example.api.repositories;

import com.example.api.domain.categorycomponent.CategoryComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryComponentRepository extends JpaRepository<CategoryComponent, Long> {

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
    CategoryComponent findCategoryComponentByChildCategoryId(Long childCategoryId);
}
