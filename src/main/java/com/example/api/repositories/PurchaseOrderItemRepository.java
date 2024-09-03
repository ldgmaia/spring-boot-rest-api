package com.example.api.repositories;

import com.example.api.domain.purchaseorderitems.PurchaseOrderItem;
import com.example.api.domain.purchaseorderitems.PurchaseOrderItemInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {

    List<PurchaseOrderItemInfoDTO> findAllByPurchaseOrderId(Long purchaseOrderId);
}
