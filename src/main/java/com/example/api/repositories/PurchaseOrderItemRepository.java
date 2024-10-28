package com.example.api.repositories;

import com.example.api.domain.purchaseorderitems.PurchaseOrderItem;
import com.example.api.domain.purchaseorderitems.PurchaseOrderItemInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {

    List<PurchaseOrderItemInfoDTO> findAllByPurchaseOrderId(Long purchaseOrderId);

    @Query("""
                SELECT poi.quantityOrdered
                FROM PurchaseOrderItem poi
                WHERE poi.id = :purchaseOrderItemId
            """)
    Long findQuantityOrderedById(@Param("purchaseOrderItemId") Long purchaseOrderItemId);
}
