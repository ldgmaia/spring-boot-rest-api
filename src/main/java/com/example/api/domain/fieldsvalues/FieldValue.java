package com.example.api.domain.fieldsvalues;

import com.example.api.domain.fields.Field;
import com.example.api.domain.values.Value;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Table(name = "fields_values")
@Entity(name = "FieldValue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class FieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "values_data_id")
    private Value valueData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fields_id")
    private Field field;

    private Double score;
    private Boolean enabled;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public FieldValue(FieldValueRegisterDTO data) {
        this.valueData = data.valueData();
        this.field = data.field();
        this.score = data.score();

        this.enabled = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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
