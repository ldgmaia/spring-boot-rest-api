package com.example.api.repositories;

import com.example.api.domain.inventoryitemscomponents.InventoryItemComponents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemComponentRepository extends JpaRepository<InventoryItemComponents, Long> {
    
}
