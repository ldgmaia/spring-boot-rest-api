package com.example.api.repositories;

import com.example.api.domain.itemtransferlog.ItemTransferLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemTransferLogRepository extends JpaRepository<ItemTransferLog, Long> {

}
