package com.example.api.repositories;

import com.example.api.domain.fields.FieldType;
import com.example.api.domain.inventoryitemsfieldsvalues.InventoryItemsFieldsValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryItemsFieldValuesRepository extends JpaRepository<InventoryItemsFieldsValues, Long> {

//    List<InventoryItemsFieldsValues> findByInventoryItemId(Long inventoryItemId);

    void deleteByInventoryItemId(Long id);

    void deleteByInventoryItemIdIn(List<Long> ids);

//    List<InventoryItemsFieldsValues> findByInventoryItemIdAndFieldValueFieldFieldType(Long id, FieldType fieldType);

    @Query("""
            select min(i.fieldValue.score)
            from InventoryItemsFieldsValues i
            where i.inventoryItem.id = :id
            and i.fieldValue.field.fieldType = :fieldType
            """)
    Long findMinScoreOfInventoryItem(@Param("id") Long id, @Param("fieldType") FieldType fieldType);


    /*
    select fv.score, iifv.*
    from inventory_items_fields_values iifv
    join fields_values fv on fv.id = iifv.field_values_id
    join fields f on fv.fields_id = f.id
    where iifv.inventory_item_id = 2
    and f.field_type = 'FUNCTIONAL'
    * */
}
