package com.example.api.repositories;

import com.example.api.domain.categoryfields.CategoryFields;
import com.example.api.domain.categoryfields.CategoryFieldsInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryFieldRepository extends JpaRepository<CategoryFields, Long> {

    List<CategoryFields> findAllByEnabledTrueAndCategoryId(Long categoryId);

    @Query(value = """
            select NEW com.example.api.domain.categoryfields.CategoryFieldsInfoDTO(cf.id, cf.dataLevel, cf.isMandatory, cf.printOnLabel, f.id, f.name, f.dataType, f.fieldType, f.isMultiple, f.enabled)
            from CategoryField cf
            join Field f on cf.field.id = f.id
            where cf.category.id = :categoryId
            and cf.enabled
            """)
    List<CategoryFieldsInfoDTO> findAllEnabledByCategoryId(Long categoryId);

    Boolean existsByCategoryId(Long categoryId);

}
