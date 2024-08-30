package com.example.api.domain.suppliers;

public record SupplierInfoDTO(
        Long id,
        String name,
        String phone,
        String email,
        String company,
        String street1,
        String street2,
        String street3,
        String city,
        String state,
        String postalCode,
        String country
) {

    public SupplierInfoDTO(Supplier supplier) {
        this(
                supplier.getId(),
                supplier.getName(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getCompany(),
                supplier.getStreet1(),
                supplier.getStreet2(),
                supplier.getStreet3(),
                supplier.getCity(),
                supplier.getState(),
                supplier.getPostalCode(),
                supplier.getCountry());
    }
}
