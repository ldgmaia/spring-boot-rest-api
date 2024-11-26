package com.example.api.domain.fields;

import com.example.api.domain.fieldgroups.FieldGroup;
import com.example.api.domain.fieldsvalues.FieldValue;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "fields")
@Entity(name = "Field")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private Boolean enabled = true;

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

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Field(FieldRegisterDTO data) {
        this.name = data.name();
        this.isMultiple = data.isMultiple();
        this.dataType = data.dataType();
        this.fieldType = data.fieldType();
        this.fieldGroup = data.fieldGroup();
    }

    public void deactivate() {
        this.enabled = false;
    }
}
