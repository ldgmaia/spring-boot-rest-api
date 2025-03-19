package com.example.api.repositories;

import com.example.api.domain.purchaseorderitems.PurchaseOrderItem;
import com.example.api.domain.purchaseorderitems.PurchaseOrderItemInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {

    List<PurchaseOrderItemInfoDTO> findAllByPurchaseOrderId(Long purchaseOrderId);

    @Query("""
            SELECT COALESCE(SUM(poi.quantityOrdered), 0)
            FROM PurchaseOrderItem poi
            WHERE poi.purchaseOrder.id = :purchaseOrderId
            """)
    Long findSumQuantityOrderedByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);
}
