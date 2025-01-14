package com.example.api.repositories;

import com.example.api.domain.inventoryitemsfieldsvalues.InventoryItemsFieldsValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryItemsFieldValuesRepository extends JpaRepository<InventoryItemsFieldsValues, Long> {

    List<InventoryItemsFieldsValues> findByInventoryItemId(Long inventoryItemId);

    void deleteByInventoryItemId(Long id);

    void deleteByInventoryItemIdIn(List<Long> ids);
}
