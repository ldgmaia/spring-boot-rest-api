package com.example.api.repositories;

import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.inventoryitems.InventoryItemsByReceivingItemDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

//    @Query("""
//                SELECT COUNT(ii)
//                FROM InventoryItem ii
//                JOIN ii.receivingItem ri
//                JOIN ri.purchaseOrderItem poi
//                WHERE poi.id = :purchaseOrderItemId
//            """)
//    Long countByPurchaseOrderItemId(@Param("purchaseOrderItemId") Long purchaseOrderItemId);

    Long countByReceivingItemId(@NotNull Long receivingItemId);

    @Query("""
            SELECT new com.example.api.domain.inventoryitems.InventoryItemsByReceivingItemDTO(
                ii, m.name, m2.name, ic.name, is2.name, l.name
            )
            FROM InventoryItem ii
            LEFT JOIN ii.mpn m
            JOIN ii.model m2
            JOIN ii.itemCondition ic
            JOIN ii.itemStatus is2
            JOIN ii.location l
            WHERE ii.receivingItem.id = :receivingItemId
            ORDER BY ii.createdAt DESC
            """)
    List<InventoryItemsByReceivingItemDTO> findByReceivingItemId(@Param("receivingItemId") Long receivingItemId);
}
