package com.example.api.repositories;

import com.example.api.domain.assessmentfieldsvalues.AssessmentFieldsValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentFieldValuesRepository extends JpaRepository<AssessmentFieldsValues, Long> {

}
