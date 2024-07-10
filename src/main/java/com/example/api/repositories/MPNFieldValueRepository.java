package com.example.api.repositories;

import com.example.api.domain.mpnfieldsvalues.MPNFieldsValues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MPNFieldValueRepository extends JpaRepository<MPNFieldsValues, Long> {

}
