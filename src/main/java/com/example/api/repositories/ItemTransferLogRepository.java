package com.example.api.repositories;

import com.example.api.domain.itemtransfer.ItemTransferLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemTransferLogRepository extends JpaRepository<ItemTransferLog, Long> {
    List<ItemTransferLog> findByItem_Id(Long itemId);
}
