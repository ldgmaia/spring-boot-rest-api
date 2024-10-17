package com.example.api.repositories;

import com.example.api.domain.itemstatus.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemStatusRepository extends JpaRepository<ItemStatus, Long> {

}
