package com.example.api.repositories;

import com.example.api.domain.sectionareas.SectionArea;
import com.example.api.domain.sectionareas.SectionAreaInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SectionAreaRepository extends JpaRepository<SectionArea, Long> {

    @Query(value = """
                SELECT NEW com.example.api.domain.sectionareas.SectionAreaInfoDTO(sa)
                FROM SectionArea sa
                WHERE sa.section.id = :sectionId
                ORDER BY areaOrder
            """)
    List<SectionAreaInfoDTO> findSectionAreasBySectionIdOrderByAreaOrder(Long sectionId);

//    @Query(value = """
//                SELECT NEW com.example.api.domain.values.ValueInfoDTO(vd.id, vd.valueData, vd.enabled, fv.score)
//                FROM Value vd
//                JOIN FieldValue fv ON vd.id = fv.valueData.id
//                WHERE fv.field.id = :fieldId
//                AND fv.enabled = true
//            """)
//    List<ValueInfoDTO> findAllEnabledValuesByFieldId(Long fieldId);

    List<SectionArea> findAllBySectionId(Long sectionId);

    // SectionAreaRepository
    Optional<SectionArea> findBySectionIdAndName(Long sectionId, String name);
}
