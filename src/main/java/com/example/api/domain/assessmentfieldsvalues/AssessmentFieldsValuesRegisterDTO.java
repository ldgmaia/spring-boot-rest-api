package com.example.api.domain.assessmentfieldsvalues;

import com.example.api.domain.assessments.Assessment;
import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.sectionareas.SectionArea;

public record AssessmentFieldsValuesRegisterDTO(
        FieldValue fieldValue,
        Assessment assessment
) {

}
