package com.example.api.repositories;

import com.example.api.domain.sectionareas.SectionArea;
import com.example.api.domain.sectionareas.SectionAreaInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionAreaRepository extends JpaRepository<SectionArea, Long> {

    @Query(value = """
                SELECT NEW com.example.api.domain.sectionareas.SectionAreaInfoDTO(sa)
                FROM SectionArea sa
                WHERE sa.section.id = :sectionId
                ORDER BY areaOrder
            """)
    List<SectionAreaInfoDTO> findSectionAreasBySectionIdOrderByAreaOrder(Long sectionId);

    Optional<SectionArea> findBySectionIdAndName(Long sectionId, String name);
}
