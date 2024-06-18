package com.example.api.repositories;

import com.example.api.domain.sections.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {


}
