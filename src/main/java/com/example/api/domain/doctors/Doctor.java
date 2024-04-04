package com.example.api.domain.doctors;

import com.example.api.domain.address.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "doctors")
@Entity(name = "Doctor")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Doctor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String cpso;
    private Boolean active;

    @Enumerated(EnumType.STRING)
    private Specialty specialty;
    @Embedded
    private Address address;

    public Doctor(DoctorRegisterDTO data) {
        this.name = data.name();
        this.email = data.email();
        this.phone = data.phone();
        this.cpso = data.cpso();
        this.active = true;
        this.specialty = data.specialty();
        this.address = new Address(data.address());
    }

    public void updateInfo(DoctorUpdateDTO data) {

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
