package com.example.api.repositories;

import com.example.api.domain.purchaseorders.PurchaseOrder;
import com.example.api.domain.suppliers.SupplierInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("""
            SELECT NEW com.example.api.domain.suppliers.SupplierInfoDTO(s)
            FROM PurchaseOrder po
            JOIN po.supplier s
            WHERE po.id = :id
            """)
    SupplierInfoDTO getSupplier(Long id);

    List<PurchaseOrder> findAllByStatusNotIn(List<String> status);

    PurchaseOrder findByQboId(Long purchaseOrderQboId);
}
