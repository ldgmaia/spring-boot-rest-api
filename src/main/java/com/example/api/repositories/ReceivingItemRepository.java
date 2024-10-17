package com.example.api.repositories;

import com.example.api.domain.assessments.PendingAssessmentListDTO;
import com.example.api.domain.receivingitems.ReceivingItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceivingItemRepository extends JpaRepository<ReceivingItem, Long> {

    @Query("SELECT COALESCE(SUM(ri.quantityReceived), 0) FROM ReceivingItem ri WHERE ri.purchaseOrderItem.id = :purchaseOrderItemId")
    Long findQuantityReceivedByPurchaseOrderItemId(@Param("purchaseOrderItemId") Long purchaseOrderItemId);

    @Modifying
    @Query("UPDATE ReceivingItem ri SET ri.status = :status WHERE ri.purchaseOrderItem.id = :purchaseOrderItemId")
    void updateStatusByPurchaseOrderItemId(@Param("purchaseOrderItemId") Long purchaseOrderItemId, @Param("status") String status);

    @Query("SELECT COALESCE(SUM(ri.quantityReceived), 0) FROM ReceivingItem ri WHERE ri.purchaseOrderItem.id = :purchaseOrderItemId")
    Long findTotalReceivedQuantityByPurchaseOrderItemId(@Param("purchaseOrderItemId") Long purchaseOrderItemId);

    @Query("""
                SELECT r 
                FROM ReceivingItem r 
                WHERE r.purchaseOrderItem.id = :purchaseOrderItemId
            """)
    ReceivingItem findByPurchaseOrderItemId(@Param("purchaseOrderItemId") Long purchaseOrderItemId);

    @Query("""
            SELECT new com.example.api.domain.assessments.PendingAssessmentListDTO(
                ri.id, po.poNumber, ri.description, ri.status, poi.quantityOrdered, ri.quantityReceived, s.name, ri.createdAt
            )
            FROM ReceivingItem ri
            JOIN ri.purchaseOrderItem poi
            JOIN poi.purchaseOrder po
            JOIN po.supplier s
            JOIN ri.receiving r
            WHERE r.type = 'PO'
            AND ri.quantityReceived > 0
            AND ri.status = :statusToFind
            """)
    Page<PendingAssessmentListDTO> findItemsByStatus(@Param("statusToFind") String statusToFind, Pageable pageable);

}
