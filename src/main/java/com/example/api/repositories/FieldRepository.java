package com.example.api.repositories;

import com.example.api.domain.fields.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    Page<Field> findAllByEnabledTrue(Pageable pagination);

    List<Field> findByEnabledTrueAndFieldGroupId(Long fieldGroupId);

    Boolean existsByName(String name);

    Field findByName(String name);
}
