package com.example.api.repositories;

import com.example.api.domain.receivingitems.ReceivingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceivingItemRepository extends JpaRepository<ReceivingItem, Long> {

    @Query("SELECT COALESCE(SUM(ri.quantityReceived), 0) FROM ReceivingItem ri WHERE ri.purchaseOrderItem.id = :purchaseOrderItemId")
    Long findQuantityReceivedByPurchaseOrderItemId(@Param("purchaseOrderItemId") Long purchaseOrderItemId);

}
