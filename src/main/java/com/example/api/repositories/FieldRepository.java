package com.example.api.repositories;

import com.example.api.domain.fields.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldRepository extends JpaRepository<Field, Long> {

    Page<Field> findAllByEnabledTrue(Pageable pagination);

    List<Field> findByEnabledTrueAndFieldGroupId(Long fieldGroupId);

    Boolean existsByName(String name);

    Field findByName(String name);
}
