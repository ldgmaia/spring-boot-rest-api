package com.example.api.domain.address;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String street;
    private String city;
    private String postalCode;
    private String province;

    public Address(AddressDTO address) {
        this.street = address.street();
        this.city = address.city();
        this.postalCode = address.postalCode();
        this.province = address.province();
    }

    public void updateInfo(AddressDTO data) {
        this.street = (data.street() != null) ? data.street() : this.street;
        this.city = (data.city() != null) ? data.city() : this.city;
        this.postalCode = (data.postalCode() != null) ? data.postalCode() : this.postalCode;
        this.province = (data.province() != null) ? data.province() : this.province;
    }
}
