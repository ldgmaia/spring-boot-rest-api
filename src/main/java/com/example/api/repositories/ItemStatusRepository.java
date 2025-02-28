package com.example.api.repositories;

import com.example.api.domain.itemstatus.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemStatusRepository extends JpaRepository<ItemStatus, Long> {

    List<ItemStatus> findByCanTransferTrue();
}
