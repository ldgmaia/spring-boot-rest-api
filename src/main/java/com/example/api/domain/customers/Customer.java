package com.example.api.domain.customers;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "customers")
@Entity(name = "Customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
//@EntityListeners(AuditingEntityListener.class)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String name;
    private String company;
    private String email;
    private String phone;
    private String username;

    @Embedded
    private Address address;

    //    @CreatedDate
    private LocalDateTime createdAt;

    //    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Customer(CustomerRegisterDTO customer) {
        this.customerId = customer.customerId();
        this.name = customer.name();
        this.company = customer.company();
        this.email = customer.email();
        this.phone = customer.phone();
        this.username = customer.username();
        this.address = customer.address();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
