package com.example.api.domain.customers;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String street1;
    private String street2;
    private String street3;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    public Address(AddressRegisterDTO data) {
        this.street1 = data.street1();
        this.street2 = data.street2();
        this.street3 = data.street3();
        this.city = data.city();
        this.state = data.state();
        this.postalCode = data.postalCode();
        this.country = data.country();
    }
}
