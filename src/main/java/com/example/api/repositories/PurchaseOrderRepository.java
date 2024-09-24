package com.example.api.repositories;

import com.example.api.domain.purchaseorders.PurchaseOrder;
import com.example.api.domain.suppliers.SupplierInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("""
            SELECT NEW com.example.api.domain.suppliers.SupplierInfoDTO(s)
            FROM PurchaseOrder po
            JOIN po.supplier s
            WHERE po.id = :id
            """)
    SupplierInfoDTO getSupplier(Long id);
}
