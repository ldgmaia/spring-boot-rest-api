package com.example.api.repositories;

import com.example.api.domain.sections.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByModelIdOrderBySectionOrder(Long modelId);
}
