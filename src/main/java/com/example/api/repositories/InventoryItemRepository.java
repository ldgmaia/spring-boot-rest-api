package com.example.api.repositories;

import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.inventoryitems.InventoryItemsByReceivingItemDTO;
import com.example.api.domain.sectionareas.SectionArea;
import com.example.api.domain.values.Value;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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

    Long countByReceivingItemIdAndTypeAndItemStatusIdNot(Long receivingItemId, String type, Long itemStatusId);

    @Query("""
            SELECT new com.example.api.domain.inventoryitems.InventoryItemsByReceivingItemDTO(
                ii, m.name, m2.name, ic.name, is2.name, sl.name
            )
            FROM InventoryItem ii
            LEFT JOIN ii.mpn m
            JOIN ii.model m2
            JOIN ii.itemCondition ic
            JOIN ii.itemStatus is2
            JOIN ii.storageLevel sl
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


//    @Query("""
//            SELECT fv.valueData
//            FROM InventoryItem ii
//            JOIN ii.inventoryItemComponents iic
//            JOIN iic.inventoryItem ii2
//            JOIN ii2.inventoryItemsFieldsValues iifv
//            JOIN iifv.fieldValue fv
//            WHERE ii.id = :inventoryItemId
//              AND fv.field.id = :fieldId
//              AND ii2.category.id = :categoryId
//            """)
//    Value findComponentFieldValueDataIdByInventoryItemIdAndFieldIdAndCategoryId(Long inventoryItemId, Long fieldId, Long categoryId);

    @Query("""
                SELECT vd
                FROM Value vd
                JOIN FieldValue fv ON vd.id = fv.valueData.id
                JOIN InventoryItemsFieldsValues iifv ON fv.id = iifv.fieldValue.id
                JOIN InventoryItem ii2 ON iifv.inventoryItem.id = ii2.id
                JOIN ii2.sectionArea sa
                JOIN ii2.category c
                JOIN InventoryItemComponent iic ON ii2.id = iic.inventoryItem.id
                JOIN InventoryItem ii ON iic.parentInventoryItem.id = ii.id
                WHERE ii.id = :parentId
                AND ii2.sectionArea = :sectionArea
                AND fv.field.id = :fieldId
            """)
    Value findValuesDataBySectionAreaAndFieldIdOfComponent(
            @Param("parentId") Long parentId,
            @Param("sectionArea") SectionArea sectionArea,
            @Param("fieldId") Long fieldId
    );


    @Query("""
            SELECT fv
            FROM InventoryItem ii
            JOIN ii.inventoryItemsFieldsValues iifv
            JOIN iifv.fieldValue fv
            WHERE ii.id = :inventoryItemId
              AND fv.field.id = :fieldId
            """)
    FieldValue findMainItemFieldValueDataIdByInventoryItemId(Long inventoryItemId, Long fieldId);

    @Query("""
            SELECT ii
            FROM InventoryItem ii
            WHERE ii.storageLevel.id = :storageLevelId
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
    List<InventoryItem> findBystorageLevelIdAndTypeAndItemStatusId(
            @Param("storageLevelId") Long storageLevelId,
            @Param("type") String type,
            @Param("itemStatusId") Long itemStatusId,
            @Param("inventoryItemId") Long inventoryItemId,
            @Param("sectionAreaId") Long sectionAreaId
    );


//    InventoryItem findByIdAndInventoryItemComponentsInventoryItemSectionAreaAndInventoryItemComponentsInventoryItemInventoryItemsFieldsValuesFieldValueFieldId(Long inventoryItemId, SectionArea sectionArea, Long fieldId);


}
