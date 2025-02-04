package com.example.api.domain.values;

import com.example.api.domain.fieldsvalues.FieldValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "values_data")
@Entity(name = "Value")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Value {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String valueData;
    private Boolean enabled;

    @OneToMany(mappedBy = "valueData", orphanRemoval = true)
    private List<FieldValue> fieldValues = new ArrayList<>();

    public Value(ValueRegisterDTO data) {
        this.valueData = data.valueData();
        this.enabled = true;
    }

    public void updateInfo(ValueUpdateDTO data) {
        this.valueData = (data.valueData() != null) ? data.valueData() : this.valueData;
    }

    public void deactivate() {
        this.enabled = false;
    }
}
