package com.example.api.repositories;


import com.example.api.domain.customers.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

//    Optional<Customer> findByUsername(String username);

    Customer findByCustomerId(Long aLong);
}
