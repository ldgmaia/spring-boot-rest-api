package com.example.api.repositories;

import com.example.api.domain.storage.storagearea.StorageArea;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageAreaRepository extends JpaRepository<StorageArea, Long> {

    List<StorageArea> findAllByStorageZoneId(Long storageZoneId);

    Optional<StorageArea> findByStorageZoneIdAndName(Long storageZoneId, @NotNull String name);
}
