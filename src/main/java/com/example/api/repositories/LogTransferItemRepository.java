package com.example.api.repositories;

import com.example.api.domain.itemtransfer.ItemTransferLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogTransferItemRepository extends JpaRepository<ItemTransferLog, Long> {

}
