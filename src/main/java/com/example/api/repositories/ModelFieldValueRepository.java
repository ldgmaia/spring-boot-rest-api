package com.example.api.repositories;

import com.example.api.domain.modelfieldsvalues.ModelFieldsValues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelFieldValueRepository extends JpaRepository<ModelFieldsValues, Long> {


}
