package com.example.api.repositories;

import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.inventoryitems.InventoryItemsByReceivingItemDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    @Query("""
                SELECT COUNT(ii)
                FROM InventoryItem ii
                JOIN ii.receivingItem ri
                JOIN ri.purchaseOrderItem poi
                WHERE poi.purchaseOrder.id = :purchaseOrderId
            """)
    Long countByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);

//    Long countByReceivingItemId(@NotNull Long receivingItemId);

    // count by receiving item id and item type
    Long countByReceivingItemIdAndType(@NotNull Long receivingItemId, @NotNull String type);

    // count by receiving item id and item type and item status not in
    Long countByReceivingItemIdAndTypeAndItemStatusIdNotIn(@NotNull Long receivingItemId, @NotNull String type, @NotNull List<Long> statusIds);

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
            AND ii.type = :type
            AND ii.itemStatus.id = :statusId
            ORDER BY ii.createdAt DESC
            """)
    List<InventoryItemsByReceivingItemDTO> findByReceivingItemId(
            @Param("receivingItemId") Long receivingItemId,
            @Param("type") String type,
            @Param("statusId") Long statusId);
}
