package com.example.api.repositories;

import com.example.api.domain.categorygroups.CategoryGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryGroupRepository extends JpaRepository<CategoryGroups, Long> {

    Page<CategoryGroups> findAllByEnabledTrue(Pageable pagination);

    Boolean existsByName(String name);

    @Query("""
            select fg.enabled
            from CategoryGroup fg
            where fg.id = :categoryGroupId
            """)
    Boolean findEnabledById(Long categoryGroupId);
}
