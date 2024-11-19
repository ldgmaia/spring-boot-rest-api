package com.example.api.domain.fields;

import com.example.api.domain.fieldgroups.FieldGroup;
import com.example.api.domain.fieldsvalues.FieldValue;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "fields")
@Entity(name = "Field")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private Boolean enabled;
    private Boolean isMultiple;

    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_groups_id")
    private FieldGroup fieldGroup;

    @OneToMany(mappedBy = "field", orphanRemoval = true)
    private List<FieldValue> fieldValues = new ArrayList<>();

//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    private LocalDateTime updatedAt;

    public Field(FieldRegisterDTO data) {
        this.name = data.name();
        this.isMultiple = data.isMultiple();
        this.dataType = data.dataType();
        this.fieldType = data.fieldType();
        this.fieldGroup = data.fieldGroup();
        this.enabled = true;
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
    }

    //    public void updateInfo(FieldUpdateDTO data) {
//
//        this.name = (data.name() != null) ? data.name() : this.name;
//        this.updatedAt = LocalDateTime.now();
//    }
//
    public void deactivate() {
        this.enabled = false;
    }
}
