package com.example.api.domain.receivingadditionalitems;

import jakarta.validation.constraints.NotBlank;

public record ReceivingAdditionalItemRequestDTO(

        @NotBlank
        String description,

        @NotBlank
        Long quantityReceived


) {

}