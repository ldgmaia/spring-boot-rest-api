package com.example.api.repositories;

import com.example.api.domain.categoryfield.CategoryField;
import com.example.api.domain.categoryfield.CategoryFieldRequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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

//    @Query(value = """
//                select cf.dataLevel, cf.field.id, cf.printOnLabel, cf.isMandatory
//                from CategoryField cf
//                where cf.category.id = :categoryId
//            """)
//    List<CategoryFieldRequestDTO> findAllFieldsByCategoryId(Long categoryId);


    List<CategoryFieldRequestDTO> findAllByEnabledTrueAndCategoryId(Long categoryId);
}
