package com.example.api.repositories;

import com.example.api.domain.receivingitems.ReceivingItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivingItemRepository extends JpaRepository<ReceivingItem, Long> {

}
