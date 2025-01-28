package com.example.api.repositories;

import com.example.api.domain.storage.storagelocation.StorageLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StorageLocationRepository extends JpaRepository<StorageLocation, Long> {
    StorageLocation findByName(String name);

    List<StorageLocation> findAllByStorageAreaId(Long id);

    Optional<StorageLocation> findByStorageAreaIdAndName(Long id, String name);
}
