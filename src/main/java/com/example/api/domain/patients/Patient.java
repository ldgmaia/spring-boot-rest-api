package com.example.api.domain.patients;

import com.example.api.domain.address.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "patients")
@Entity(name = "Patient")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Boolean active;

    @Embedded
    private Address address;

    public Patient(PatientRegisterDTO data) {
        this.name = data.name();
        this.email = data.email();
        this.phone = data.phone();
        this.active = true;
        this.address = new Address(data.address());
    }

    public void updateInfo(PatientUpdateDTO data) {

        this.name = (data.name() != null) ? data.name() : this.name;
        this.phone = (data.phone() != null) ? data.phone() : this.phone;

        if (data.address() != null) {
            this.address.updateInfo(data.address());
        }
    }

    public void deactivate() {
        this.active = false;
    }
}
