package com.example.api.repositories;

import com.example.api.domain.storage.storagezone.StorageZone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageZoneRepository extends JpaRepository<StorageZone, Long> {
    StorageZone findByName(String name);
}
