package com.example.api.repositories;

import com.example.api.domain.mpnfieldsvalues.MPNFieldsValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MPNFieldValueRepository extends JpaRepository<MPNFieldsValues, Long> {
    List<MPNFieldsValues> findAllByMpnId(Long mpnId);
}
