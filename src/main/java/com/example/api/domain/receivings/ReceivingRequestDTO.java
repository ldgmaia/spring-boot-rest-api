package com.example.api.domain.receivings;

import com.example.api.domain.receivingitems.ReceivingItemRequestDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ReceivingRequestDTO(

        String trackingCode,

        @NotNull
        ReceivingType type,

        @NotNull
        Long identifierId,

        @NotNull
        Long supplierId,

        Long carrierId,

        String notes,

        MultipartFile[] pictures,

        @NotNull
        List<ReceivingItemRequestDTO> receivingItems
//        ReceivingItemRequestDTO[] receivingItems
) {

}
