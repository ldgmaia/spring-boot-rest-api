package com.example.api.domain.values;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Value(ValueRegisterDTO data) {
        this.valueData = data.valueData();
        this.enabled = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateInfo(ValueUpdateDTO data) {

        this.valueData = (data.valueData() != null) ? data.valueData() : this.valueData;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.enabled = false;
    }
}
