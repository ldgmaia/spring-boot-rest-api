package com.example.api.repositories;

import com.example.api.domain.inventoryitemsfieldsvalues.InventoryItemsFieldsValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemsFieldValuesRepository extends JpaRepository<InventoryItemsFieldsValues, Long> {

}
