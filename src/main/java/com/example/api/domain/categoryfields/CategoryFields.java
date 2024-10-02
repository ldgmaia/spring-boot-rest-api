package com.example.api.domain.categoryfields;

import com.example.api.domain.categories.Category;
import com.example.api.domain.fields.Field;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "category_fields")
@Entity(name = "CategoryField")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CategoryFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DataLevel dataLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fields_id")
    private Field field;

    private Boolean isMandatory;
    private Long orderOnLabel;
    private Boolean printOnLabel;
    private Boolean enabled;

    public CategoryFields(CategoryFieldsRegisterDTO data) {
        this.dataLevel = data.dataLevel();
        this.category = data.category();
        this.field = data.field();
        this.enabled = true;
        this.isMandatory = data.isMandatory();
        this.printOnLabel = data.printOnLabel();
    }
}
