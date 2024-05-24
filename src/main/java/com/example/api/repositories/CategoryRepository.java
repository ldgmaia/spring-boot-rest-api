package com.example.api.repositories;

import com.example.api.domain.categories.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

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
    Boolean existsByName(String name);

    Page<Category> findAllByEnabledTrue(Pageable pagination);

}
