package com.example.api.repositories;

import com.example.api.domain.purchaseorders.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

//    Page<Category> findAllByEnabledTrue(Pageable pagination);

}
