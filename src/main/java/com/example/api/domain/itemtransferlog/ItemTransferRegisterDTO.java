package com.example.api.domain.itemtransferlog;

import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.storage.storagelevel.StorageLevel;

public record ItemTransferRegisterDTO(
        InventoryItem inventoryItem,
        StorageLevel fromStorageLevel,
        StorageLevel toStorageLevel,
        TransferStatus transferStatus,
        String message
) {
}
