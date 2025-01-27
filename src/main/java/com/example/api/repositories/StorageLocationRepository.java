package com.example.api.repositories;

import com.example.api.domain.storage.storagelocation.StorageLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageLocationRepository extends JpaRepository<StorageLocation, Long> {
    StorageLocation findByName(String name);
}
