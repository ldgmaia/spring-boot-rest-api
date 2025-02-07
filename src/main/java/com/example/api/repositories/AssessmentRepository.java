package com.example.api.repositories;

import com.example.api.domain.assessments.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

//    List<Assessment> findByParentInventoryItemId(Long id);
}
