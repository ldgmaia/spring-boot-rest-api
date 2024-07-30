package com.example.api.domain.suppliers;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "suppliers")
@Entity(name = "Supplier")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String company;
    private String street1;
    private String street2;
    private String street3;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    public Supplier(SupplierRegisterDTO data) {
        this(
                null,  // id will be auto-generated
                data.name(),
                data.phone(),
                data.email(),
                data.company(),
                data.street1(),
                data.street2(),
                data.street3(),
                data.city(),
                data.state(),
                data.postalCode(),
                data.country()
        );
    }
}
