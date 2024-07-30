package com.example.api.repositories;

import com.example.api.domain.suppliers.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

}
