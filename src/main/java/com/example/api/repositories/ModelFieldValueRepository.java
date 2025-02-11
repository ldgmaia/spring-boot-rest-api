package com.example.api.repositories;

import com.example.api.domain.modelfieldsvalues.ModelFieldValueInfoDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldsValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelFieldValueRepository extends JpaRepository<ModelFieldsValues, Long> {

    @Query(value = """
            SELECT NEW com.example.api.domain.modelfieldsvalues.ModelFieldValueInfoDTO(
                mfv.id, f.id, f.name, vd.id, vd.valueData)
            FROM ModelFieldsValues mfv
            JOIN mfv.fieldValue fv
            JOIN fv.field f
            JOIN fv.valueData vd
            WHERE mfv.model.id = :modelId
            """)
    List<ModelFieldValueInfoDTO> findFieldsValuesByModelId(Long modelId);

    List<ModelFieldsValues> findAllByModelId(Long modelId);
}
