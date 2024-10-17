package com.example.api.domain.receivingitems;

import com.example.api.domain.ValidationException;
import com.example.api.repositories.ReceivingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceivingItemService {

    @Autowired
    private ReceivingItemRepository receivingItemRepository;

    public ReceivingItemInfoDTO show(Long id) {
        var receivingItem = receivingItemRepository.findById(id).orElseThrow(() -> new ValidationException("Receiving not found"));
        return new ReceivingItemInfoDTO(receivingItem);
    }

}
