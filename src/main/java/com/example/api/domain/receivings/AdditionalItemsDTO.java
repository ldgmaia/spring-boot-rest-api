package com.example.api.domain.receivings;

public record AdditionalItemsDTO(
        String description,
        Long quantityReceived
) {

    public AdditionalItemsDTO(AdditionalItemsDTO AddItemDTO) {
        this(AddItemDTO.description(),
                AddItemDTO.quantityReceived());
    }

}
