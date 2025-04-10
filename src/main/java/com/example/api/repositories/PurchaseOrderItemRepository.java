package com.example.api.repositories;

import com.example.api.domain.purchaseorderitems.PurchaseOrderItem;
import com.example.api.domain.purchaseorderitems.PurchaseOrderItemInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {

    List<PurchaseOrderItemInfoDTO> findAllByPurchaseOrderId(Long purchaseOrderId);

    @Query(
            nativeQuery = true, value = """
            SELECT COALESCE(SUM(poi.quantity_ordered), 0)
            FROM purchase_order_items poi
            join receiving_items ri on ri.purchase_order_item_id = poi.id
            WHERE poi.purchase_orders_id = :purchaseOrderId
            and ri.receivable_item
            """)
    Long findSumQuantityOrderedByPurchaseOrderId(Long purchaseOrderId);
}
