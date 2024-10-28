package com.example.api.domain.sectionareamodels;

import com.example.api.domain.models.Model;
import com.example.api.domain.sectionareas.SectionArea;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "section_areas_models", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"section_areas_id", "models_id"})
})
@Entity(name = "SectionAreaModel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SectionAreaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_areas_id")
    private SectionArea sectionArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "models_id")
    private Model model;

    public SectionAreaModel(SectionAreaModelRegisterDTO data) {
        this.sectionArea = data.sectionArea();
        this.model = data.model();
    }
}
