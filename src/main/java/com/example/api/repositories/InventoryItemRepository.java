package com.example.api.repositories;

import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.inventoryitems.InventoryItemsByReceivingItemDTO;
import com.example.api.domain.values.Value;
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
            ORDER BY ii.createdAt DESC
            """)
    List<InventoryItemsByReceivingItemDTO> findByReceivingItemId(
            @Param("receivingItemId") Long receivingItemId,
            @Param("type") String type);

    InventoryItem findBySerialNumber(String serialNumber);

    @Query("""
            SELECT ii2
            FROM InventoryItem ii
            JOIN ii.inventoryItemComponents iic
            JOIN iic.inventoryItem ii2
            WHERE ii.id = :inventoryItemId
              AND ii2.sectionArea.id = :sectionAreaId
            """)
    InventoryItem findComponentModelIdBySectionAreaId(Long inventoryItemId, Long sectionAreaId);

//    @Query("""
//            SELECT ii2
//            FROM InventoryItem ii
//            JOIN ii.inventoryItemComponents iic
//            JOIN iic.inventoryItem ii2
//            WHERE ii.id = :inventoryItemId
//              AND ii2.sectionArea.id = :sectionAreaId
//            """)
//    List<InventoryItem> findComponentsBySectionAreaId(Long inventoryItemId, Long sectionAreaId);


    @Query("""
            SELECT fv.valueData
            FROM InventoryItem ii
            JOIN ii.inventoryItemComponents iic
            JOIN iic.inventoryItem ii2
            JOIN ii2.inventoryItemsFieldsValues iifv
            JOIN iifv.fieldValue fv
            WHERE ii.id = :inventoryItemId
              AND fv.field.id = :fieldId
              AND ii2.category.id = :categoryId
            """)
    Value findComponentFieldValueDataIdByInventoryItemIdAndFieldIdAndCategoryId(Long inventoryItemId, Long fieldId, Long categoryId);

    @Query("""
            SELECT fv.valueData
            FROM InventoryItem ii
            JOIN ii.inventoryItemsFieldsValues iifv
            JOIN iifv.fieldValue fv
            WHERE ii.id = :inventoryItemId
              AND fv.field.id = :fieldId
            """)
    Value findMainItemFieldValueDataIdByInventoryItemId(Long inventoryItemId, Long fieldId);

    @Query("""
            SELECT ii
            FROM InventoryItem ii
            WHERE ii.location.id = :locationId
              AND ii.type = :type
              AND ii.itemStatus.id = :itemStatusId
            and ii.model.id in (
            	select sam.model.id
            	from InventoryItem ii2
            	left join Section s on s.model.id = ii2.model.id
            	left join SectionArea sa on sa.section.id = s.id
            	left join SectionAreaModel sam on sa.id = sam.sectionArea.id
            	where ii2.id = :inventoryItemId
            	and sa.id = :sectionAreaId
            )
            """)
    List<InventoryItem> findByLocationIdAndTypeAndItemStatusId(
            @Param("locationId") Long locationId,
            @Param("type") String type,
            @Param("itemStatusId") Long itemStatusId,
            @Param("inventoryItemId") Long inventoryItemId,
            @Param("sectionAreaId") Long sectionAreaId
    );
}
