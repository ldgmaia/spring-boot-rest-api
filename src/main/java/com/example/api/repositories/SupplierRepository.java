package com.example.api.repositories;

import com.example.api.domain.suppliers.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    Supplier findByQboId(long qboId);

    Boolean existsByQboId(Long vendorQboId);
}
