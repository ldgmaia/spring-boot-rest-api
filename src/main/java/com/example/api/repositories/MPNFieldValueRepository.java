package com.example.api.repositories;

import com.example.api.domain.mpnfieldsvalues.MPNFieldValueInfoDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldsValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MPNFieldValueRepository extends JpaRepository<MPNFieldsValues, Long> {
    List<MPNFieldsValues> findAllByMpnId(Long mpnId);

    @Query(value = """
            SELECT NEW com.example.api.domain.mpnfieldsvalues.MPNFieldValueInfoDTO(
                mfv.id, f.id, f.name, vd.id, vd.valueData)
            FROM MPNFieldsValues mfv
            JOIN mfv.fieldValue fv
            JOIN fv.field f
            JOIN fv.valueData vd
            WHERE mfv.mpn.id = :mpnId
            """)
    List<MPNFieldValueInfoDTO> findFieldsValuesByMpnId(Long mpnId);


}
