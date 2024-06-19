package com.example.api.repositories;

import com.example.api.domain.sections.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {


//    @Query(value = """
//                SELECT NEW com.example.api.domain.sections.SectionInfoDTO(s.id, s.name, s.sectionOrder)
//                FROM Section s
//                WHERE s.model.id = :modelId
//            """)
//    List<SectionInfoDTO> findAllByModelsId(Long modelId);

    List<Section> findAllByModelId(Long modelId);

//    @Query(value = """
//            SELECT new com.example.api.domain.sections.SectionWithAreasDTO(
//                s.id, s.name, s.sectionOrder,
//                (SELECT new com.example.api.domain.sectionareas.SectionAreaInfoDTO(
//                    a.id, a.name, a.areaOrder, a.printOnLabel, a.printAreaNameOnLabel, a.orderOnLabel, a.isCritical
//                )
//                FROM SectionArea a
//                WHERE a.section.id = s.id)
//            )
//            FROM Section s
//            WHERE s.model.id = :modelId
//            """)
//    List<SectionWithAreasDTO> findAllSectionWithAreasByModelsId(@Param("modelId") Long modelId);

}
