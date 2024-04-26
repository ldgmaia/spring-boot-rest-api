package com.example.api.domain.fieldgroups;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Table(name = "field_groups")
@Entity(name = "FieldGroup")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class FieldGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean enabled;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public FieldGroup(FieldGroupRegisterDTO data) {
        this.name = data.name();
        this.enabled = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateInfo(FieldGroupUpdateDTO data) {

        this.name = (data.name() != null) ? data.name() : this.name;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.enabled = false;
    }
}
