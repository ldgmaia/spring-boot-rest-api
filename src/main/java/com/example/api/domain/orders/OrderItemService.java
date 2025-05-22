package com.example.api.domain.orders;

import com.example.api.repositories.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public void upsertOrderItems(List<OrderResponseDTO.Item> incomingItems, Order order) {
        // Fetch existing items for the order
        List<OrderItem> existingItems = orderItemRepository.findByOrderOrderId(order.getOrderId());

        // Map of existing items by their external orderItemId
        Map<Long, OrderItem> existingItemMap = existingItems.stream()
                .collect(Collectors.toMap(OrderItem::getOrderItemId, item -> item));

        List<OrderItem> itemsToSave = new ArrayList<>();
        Set<Long> incomingItemIds = new HashSet<>();

        for (OrderResponseDTO.Item itemDTO : incomingItems) {
            Long orderItemId = itemDTO.orderItemId();
            incomingItemIds.add(orderItemId);

            OrderItem existingItem = existingItemMap.get(orderItemId);

            if (existingItem == null) {
                // Create new item
                OrderItem newItem = new OrderItem(
                        new OrderItemRegisterDTO(
                                order,
                                orderItemId,
                                itemDTO.lineItemKey(),
                                itemDTO.sku(),
                                itemDTO.name(),
                                itemDTO.imageUrl(),
                                itemDTO.quantity(),
                                itemDTO.unitPrice(),
                                itemDTO.taxAmount(),
                                itemDTO.shippingAmount(),
                                itemDTO.createDate(),
                                itemDTO.modifyDate()
                        )
                );
                itemsToSave.add(newItem);
            } else {
                // Update existing item
                existingItem.setLineItemKey(itemDTO.lineItemKey());
                existingItem.setSku(itemDTO.sku());
                existingItem.setName(itemDTO.name());
                existingItem.setImageUrl(itemDTO.imageUrl());
                existingItem.setQuantity(itemDTO.quantity());
                existingItem.setUnitPrice(itemDTO.unitPrice());
                existingItem.setTaxAmount(itemDTO.taxAmount());
                existingItem.setShippingAmount(itemDTO.shippingAmount());

                itemsToSave.add(existingItem);
            }
        }

        // Save all new and updated items
        orderItemRepository.saveAll(itemsToSave);

        // Delete items no longer present in the incoming list
        List<OrderItem> itemsToDelete = existingItems.stream()
                .filter(item -> !incomingItemIds.contains(item.getOrderItemId()))
                .toList();

        orderItemRepository.deleteAll(itemsToDelete);
    }
}
