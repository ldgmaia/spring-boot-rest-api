package com.example.api.repositories;

import com.example.api.domain.inventoryitems.Inventory;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Long countByReceivingItemId(@NotNull Long receivingItemId);
}
