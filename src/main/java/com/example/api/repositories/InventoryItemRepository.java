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

    Long countByReceivingItemIdAndType(@NotNull Long receivingItemId, @NotNull String type);

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

    InventoryItem findBySerialNumber(String serialNumber);

//    InventoryItem findByRbid(String rbid);

    @Query("""
            SELECT ii2
            FROM InventoryItem ii
            JOIN ii.inventoryItemComponents iic
            JOIN iic.inventoryItem ii2
            WHERE ii.id = :inventoryItemId
              AND ii2.sectionArea.id = :sectionAreaId
            """)
    InventoryItem findComponentModelIdBySectionAreaId(Long inventoryItemId, Long sectionAreaId);

    @Query("""
            SELECT fv.valueData.id
            FROM InventoryItem ii
            JOIN ii.inventoryItemComponents iic
            JOIN iic.inventoryItem ii2
            JOIN ii2.inventoryItemsFieldsValues iifv
            JOIN iifv.fieldValue fv
            WHERE ii.id = :inventoryItemId
              AND fv.field.id = :fieldId
              AND ii2.category.id = :categoryId
            """)
    Long findComponentFieldValueDataIdByInventoryItemIdAndFieldIdAndCategoryId(Long inventoryItemId, Long fieldId, Long categoryId);

    @Query("""
            SELECT fv.valueData.id
            FROM InventoryItem ii
            JOIN ii.inventoryItemsFieldsValues iifv
            JOIN iifv.fieldValue fv
            WHERE ii.id = :inventoryItemId
              AND fv.field.id = :fieldId
            """)
    Long findMainItemFieldValueDataIdByInventoryItemId(Long inventoryItemId, Long fieldId);

}
