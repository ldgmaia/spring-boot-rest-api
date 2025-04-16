package com.example.api.domain.itemtransferlog;

import java.time.format.DateTimeFormatter;

public record ItemTransferInfoDTO(
        String fromStorageLevelName,
        String toStorageLevelName,
        String formattedCreatedAt
) {
    public ItemTransferInfoDTO(ItemTransferLog itemTransferLog) {
        this(
                itemTransferLog.getFromStorageLevel().getName(),
                itemTransferLog.getToStorageLevel().getName(),
                itemTransferLog.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a"))
        );
    }
}
