package com.example.api.repositories;

import com.example.api.domain.receivingadditionalitems.ReceivingAdditionalItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivingAdditionalItemRepository extends JpaRepository<ReceivingAdditionalItem, Long> {

}
