package com.example.api.domain.mpnfieldsvalues;

import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.mpns.MPN;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "mpns_field_values")
@Entity(name = "MPNFieldsValues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class MPNFieldsValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fields_values_id")
    private FieldValue fieldValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mpns_id")
    private MPN mpn;

    public MPNFieldsValues(MPNFieldValueRegisterDTO data) {
        this.fieldValue = data.fieldValue();
        this.mpn = data.mpn();
    }
}
