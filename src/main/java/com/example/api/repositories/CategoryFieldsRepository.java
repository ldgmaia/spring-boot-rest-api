package com.example.api.repositories;

import com.example.api.domain.categoryfield.CategoryField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryFieldsRepository extends JpaRepository<CategoryField, Long> {

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
