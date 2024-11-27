package com.example.api.domain.assessmentfieldsvalues;

import com.example.api.domain.assessments.Assessment;
import com.example.api.domain.fieldsvalues.FieldValue;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "assessments_fields_values")
@Entity(name = "AssessmentFieldsValues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class AssessmentFieldsValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_values_id")
    private FieldValue fieldValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public AssessmentFieldsValues(AssessmentFieldsValuesRegisterDTO data) {
        this.fieldValue = data.fieldValue();
        this.assessment = data.assessment();
    }
}
