package com.example.api.domain.receivingitems;

import jakarta.validation.constraints.NotBlank;

public record ReceivingItemListByRequestDTO(

        @NotBlank
        String criteria,
        
        String value,

        String[] status
) {

}
