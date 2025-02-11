package com.example.api.repositories;

import com.example.api.domain.storage.storagezone.StorageZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageZoneRepository extends JpaRepository<StorageZone, Long> {
    StorageZone findByName(String name);
}
