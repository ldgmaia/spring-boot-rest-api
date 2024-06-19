package com.example.api.repositories;

import com.example.api.domain.sectionareas.SectionArea;
import com.example.api.domain.sectionareas.SectionAreaInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectionAreaRepository extends JpaRepository<SectionArea, Long> {

    @Query(value = """
                SELECT NEW com.example.api.domain.sectionareas.SectionAreaInfoDTO(sa.id, sa.name, sa.areaOrder, sa.printOnLabel, sa.printAreaNameOnLabel, sa.orderOnLabel, sa.isCritical)
                FROM SectionArea sa
                WHERE sa.section.id = :sectionId
            """)
    List<SectionAreaInfoDTO> findAllBySectionId(Long sectionId);

//    @Query(value = """
//                SELECT NEW com.example.api.domain.values.ValueInfoDTO(vd.id, vd.valueData, vd.enabled, fv.score)
//                FROM Value vd
//                JOIN FieldValue fv ON vd.id = fv.valueData.id
//                WHERE fv.field.id = :fieldId
//                AND fv.enabled = true
//            """)
//    List<ValueInfoDTO> findAllEnabledValuesByFieldId(Long fieldId);

}
