package com.example.api.domain.storage;

import com.example.api.domain.storage.storagearea.StorageArea;
import com.example.api.domain.storage.storagearea.StorageAreaMoveDTO;
import com.example.api.domain.storage.storagelevel.StorageLevel;
import com.example.api.domain.storage.storagelevel.StorageLevelMoveDTO;
import com.example.api.domain.storage.storagelocation.StorageLocation;
import com.example.api.domain.storage.storagelocation.StorageLocationMoveDTO;
import com.example.api.domain.storage.storagezone.StorageZone;
import com.example.api.domain.storage.storagezone.StorageZoneInfoDTO;
import com.example.api.infra.exception.UniqueConstraintViolationException;
import com.example.api.repositories.StorageAreaRepository;
import com.example.api.repositories.StorageLevelRepository;
import com.example.api.repositories.StorageLocationRepository;
import com.example.api.repositories.StorageZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StorageService {

    @Autowired
    private StorageZoneRepository storageZoneRepository;

    @Autowired
    private StorageAreaRepository storageAreaRepository;

    @Autowired
    private StorageLocationRepository storageLocationRepository;

    @Autowired
    private StorageLevelRepository storageLevelRepository;

    public StorageZoneInfoDTO register(StorageRequestDTO data) {

        StorageZone zone = new StorageZone(data);
        storageZoneRepository.save(zone);

        data.areas().forEach(area -> {
            StorageArea areaEntity = new StorageArea(area, zone);
            storageAreaRepository.save(areaEntity);

            area.locations().forEach(location -> {
                StorageLocation locationEntity = new StorageLocation(location, areaEntity);
                storageLocationRepository.save(locationEntity);

                location.levels().forEach(level -> {
                    StorageLevel levelEntity = new StorageLevel(level.name(), locationEntity);
                    storageLevelRepository.save(levelEntity);
                });
            });
        });
        return new StorageZoneInfoDTO(zone);
    }

    public StorageZoneInfoDTO update(Long storageZoneId, StorageUpdateDTO data) {
        // Fetch the storage zone by ID
        var storageZone = storageZoneRepository.findById(storageZoneId)
                .orElseThrow(() -> new RuntimeException("Storage Zone not found"));

        // Update storage zone details
        storageZone.setName(data.name());
        storageZone.setDescription(data.description());

        // Handle storage zone areas
        Map<Long, StorageArea> existingAreasMap = storageAreaRepository.findAllByStorageZoneId(storageZoneId).stream()
                .collect(Collectors.toMap(StorageArea::getId, area -> area));

        Map<Long, StorageArea> newAreasMap = new HashMap<>();

        data.areas().forEach(areaDTO -> {
            StorageArea area;

            if (areaDTO.id() != null) {
                // Update existing area
                area = existingAreasMap.get(areaDTO.id());
                if (area == null) {
                    throw new RuntimeException("Area not found with ID: " + areaDTO.id());
                }
                area.setName(areaDTO.name());
                area.setDescription(areaDTO.description());
            } else {
                // Check for duplicates by name within the same storage zone
                Optional<StorageArea> existingAreaOpt = storageAreaRepository.findByStorageZoneIdAndName(storageZoneId, areaDTO.name());
                if (existingAreaOpt.isPresent()) {
                    throw new UniqueConstraintViolationException(
                            MessageFormat.format("Duplicate area \"{0}\" in storage zone \"{1}\"", areaDTO.name(), storageZone.getName()));
                }
                // Create new area
                area = new StorageArea(areaDTO, storageZone);
                storageAreaRepository.save(area);
            }

            // Handle locations within the area
            Map<Long, StorageLocation> existingLocationsMap = storageLocationRepository.findAllByStorageAreaId(area.getId()).stream()
                    .collect(Collectors.toMap(StorageLocation::getId, location -> location));

            Map<Long, StorageLocation> newLocationsMap = new HashMap<>();

            areaDTO.locations().forEach(locationDTO -> {
                StorageLocation location;

                if (locationDTO.id() != null) {
                    // Update existing location
                    location = existingLocationsMap.get(locationDTO.id());
                    if (location == null) {
                        throw new RuntimeException("Location not found with ID: " + locationDTO.id());
                    }
                    location.setName(locationDTO.name());
                    location.setDescription(locationDTO.description());
                } else {
                    // Persist the area before creating new locations
                    if (area.getId() == null) {
                        storageAreaRepository.save(area);
                    }

                    // Check for duplicates by name within the same area
                    Optional<StorageLocation> existingLocationOpt = storageLocationRepository.findByStorageAreaIdAndName(area.getId(), locationDTO.name());
                    if (existingLocationOpt.isPresent()) {
                        throw new UniqueConstraintViolationException(
                                MessageFormat.format("Duplicate location \"{0}\" in area \"{1}\"", locationDTO.name(), area.getName()));
                    }
                    // Create and associate new location
                    location = new StorageLocation(locationDTO, area);
                    storageLocationRepository.save(location);
                }

                // Handle levels within the location
                Map<Long, StorageLevel> existingLevelsMap = storageLevelRepository.findAllByStorageLocationId(location.getId()).stream()
                        .collect(Collectors.toMap(StorageLevel::getId, level -> level));

                Map<Long, StorageLevel> newLevelsMap = new HashMap<>();

                locationDTO.levels().forEach(levelDTO -> {
                    StorageLevel level;

                    if (levelDTO.id() != null) {
                        // Update existing level
                        level = existingLevelsMap.get(levelDTO.id());
                        if (level == null) {
                            throw new RuntimeException("Level not found with ID: " + levelDTO.id());
                        }
                        level.setName(levelDTO.name());
                    } else {
                        // Persist the location before creating new levels
                        if (location.getId() == null) {
                            storageLocationRepository.save(location);
                        }

                        // Check for duplicates by name within the same location
                        Optional<StorageLevel> existingLevelOpt = storageLevelRepository.findByStorageLocationIdAndName(location.getId(), levelDTO.name());
                        if (existingLevelOpt.isPresent()) {
                            throw new UniqueConstraintViolationException(
                                    MessageFormat.format("Duplicate level \"{0}\" in location \"{1}\"", levelDTO.name(), location.getName()));
                        }
                        // Create and associate new level
                        level = new StorageLevel(levelDTO.name(), location);
                        storageLevelRepository.save(level);
                    }

                    newLevelsMap.put(level.getId(), level);
                });

                // Remove levels no longer present in the update for this location
                for (Long id : existingLevelsMap.keySet()) {
                    if (!newLevelsMap.containsKey(id)) {
                        System.out.println("id " + id);
                        storageLevelRepository.deleteById(id);
                    }
                }

                newLocationsMap.put(location.getId(), location);
            });

            // Remove locations no longer present in the update for this area
            for (Long id : existingLocationsMap.keySet()) {
                if (!newLocationsMap.containsKey(id)) {
                    storageLocationRepository.deleteById(id);
                }
            }

            newAreasMap.put(area.getId(), area);
        });

        // Remove areas no longer present in the update
        for (Long id : existingAreasMap.keySet()) {
            if (!newAreasMap.containsKey(id)) {
                storageAreaRepository.deleteById(id);
            }
        }
        storageZoneRepository.save(storageZone);
        return new StorageZoneInfoDTO(storageZone);
    }

    public List<StorageZoneInfoDTO> getAllZonesInfo() {
        List<StorageZone> zones = storageZoneRepository.findAll();
        return zones.stream()
                .map(StorageZoneInfoDTO::new)
                .toList();
    }

    public StorageZoneInfoDTO getZoneInfo(Long id) {
        StorageZone zone = storageZoneRepository.getReferenceById(id);
        return new StorageZoneInfoDTO(zone);
    }

    public void moveStorageLevel(StorageLevelMoveDTO request) {
        StorageLevel storageLevel = storageLevelRepository.findById(request.levelId()).orElseThrow(() -> new RuntimeException("Storage Level not found"));
        StorageLocation newLocation = storageLocationRepository.findById(request.newLocationId()).orElseThrow(() -> new RuntimeException("Target Storage Location not found"));
        storageLevel.setStorageLocation(newLocation);
        storageLevelRepository.save(storageLevel);
    }

    public void moveStorageLocation(StorageLocationMoveDTO request) {
        StorageLocation storageLocation = storageLocationRepository.findById(request.locationId()).orElseThrow(() -> new RuntimeException("Storage Location not found"));
        StorageArea newArea = storageAreaRepository.findById(request.newAreaId()).orElseThrow(() -> new RuntimeException("Target Storage Area not found"));
        storageLocation.setStorageArea(newArea);
        storageLocationRepository.save(storageLocation);
    }

    public void moveStorageArea(StorageAreaMoveDTO request) {
        StorageArea storageArea = storageAreaRepository.findById(request.areaId()).orElseThrow(() -> new RuntimeException("Storage Area not found"));
        StorageZone newZone = storageZoneRepository.findById(request.newZoneId()).orElseThrow(() -> new RuntimeException("Target Storage Zone not found"));
        storageArea.setStorageZone(newZone);
        storageAreaRepository.save(storageArea);
    }
}
