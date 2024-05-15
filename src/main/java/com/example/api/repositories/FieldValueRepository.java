package com.example.api.repositories;

import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.values.ValueInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FieldValueRepository extends JpaRepository<FieldValue, Long> {


//    FieldValue findById(Long fieldValueId);

//    Boolean existsByName(String name);

//    Boolean existsByValuesDataIdAndFieldsId(Long valueDataId, Long fieldId);

    @Query(value = """
            SELECT EXISTS (
                SELECT 1
                FROM fields_values fv
                WHERE fv.values_data_id = :valuesDataId
                AND fv.fields_id = :fieldsId
            ) AS fvexists;
            """, nativeQuery = true)
    int countByValuesDataIdAndFieldsId(Long valuesDataId, Long fieldsId);

    default Boolean existsByValuesDataIdAndFieldsId(Long valuesDataId, Long fieldsId) {
        return countByValuesDataIdAndFieldsId(valuesDataId, fieldsId) > 0;
    }

    @Query(value = """
                SELECT NEW com.example.api.domain.values.ValueInfoDTO(vd.id, vd.valueData, vd.enabled, fv.score)
                FROM Value vd
                JOIN FieldValue fv ON vd.id = fv.valueData.id
                WHERE fv.field.id = :fieldId
                AND fv.enabled = true
            """)
    List<ValueInfoDTO> findAllEnabledValuesByFieldId(Long fieldId);

}
