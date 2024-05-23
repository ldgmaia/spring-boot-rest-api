package com.example.api.repositories;

import com.example.api.domain.categorycomponent.CategoryComponent;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
