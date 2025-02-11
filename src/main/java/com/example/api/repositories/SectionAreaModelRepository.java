package com.example.api.repositories;

import com.example.api.domain.sectionareamodels.SectionAreaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionAreaModelRepository extends JpaRepository<SectionAreaModel, Long> {
    List<SectionAreaModel> findBySectionAreaId(Long sectionAreaId);

    @Query("SELECT m.model.id FROM SectionAreaModel m WHERE m.sectionArea.id = :sectionAreaId")
    List<Long> findModelIdsBySectionAreaId(@Param("sectionAreaId") Long sectionAreaId);
}
