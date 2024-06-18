package com.example.api.domain.modelfieldsvalues;

import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.models.Model;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "model_fields_values")
@Entity(name = "ModelFieldsValues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ModelFieldsValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fields_values_id")
    private FieldValue fieldValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "models_id")
    private Model model;

    public ModelFieldsValues(ModelFieldValueRegisterDTO data) {
        this.fieldValue = data.fieldValue();
        this.model = data.model();
    }
}
