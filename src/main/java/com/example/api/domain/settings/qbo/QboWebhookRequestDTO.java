package com.example.api.domain.settings.qbo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QboWebhookRequestDTO(
        @NotEmpty List<EventNotification> eventNotifications
) {
    public record EventNotification(
            @NotNull String realmId,
            @NotNull DataChangeEvent dataChangeEvent
    ) {
    }

    public record DataChangeEvent(
            @NotEmpty List<Entity> entities
    ) {
    }

    public record Entity(
            @NotNull String id,
            @NotNull String operation,
            @NotNull String name,
            @NotNull String lastUpdated
    ) {
    }
}
