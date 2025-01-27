package com.example.api.domain.storage;

import com.example.api.domain.storage.storagearea.StorageArea;
import com.example.api.domain.storage.storagelevel.StorageLevel;
import com.example.api.domain.storage.storagelocation.StorageLocation;
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
                    StorageLevel levelEntity = new StorageLevel(level, locationEntity);
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
            }

            storageAreaRepository.save(area);
            newAreasMap.put(area.getId(), area);
        });

        // Remove areas no longer present in the update
        for (Long id : existingAreasMap.keySet()) {
            if (!newAreasMap.containsKey(id)) {
                System.out.println("deleting area " + id);
                storageAreaRepository.deleteById(id);
            }
        }
        storageZoneRepository.save(storageZone);
        return new StorageZoneInfoDTO(storageZone);
    }

    // Method to get all zones and map them to DTOs
    public List<StorageZoneInfoDTO> getAllZonesInfo() {
        List<StorageZone> zones = storageZoneRepository.findAll();
        return zones.stream()
                .map(StorageZoneInfoDTO::new)
                .toList();
    }
//    public StorageZone update(Long storageZoneId, StorageUpdateDTO updatedData) {
//        // Fetch the existing storage zone
//        StorageZone existingZone = storageZoneRepository.findById(storageZoneId)
//                .orElseThrow(() -> new IllegalArgumentException("Storage Zone not found"));
//
//        // Update the zone properties
//        existingZone.setName(updatedData.name());
//        existingZone.setDescription(updatedData.description());
//        storageZoneRepository.save(existingZone);
//
//        // Process areas
//        Map<Long, StorageArea> existingAreas = existingZone.getAreas()
//                .stream()
//                .collect(Collectors.toMap(StorageArea::getId, area -> area));
//        System.out.println("existingAreas " + existingAreas);
//
//        for (StorageAreaUpdateDTO updatedArea : updatedData.areas()) {
//            StorageArea areaEntity;
//
//            System.out.println("updatedArea " + updatedArea.id());
//
//            if (updatedArea.id() != null && existingAreas.containsKey(updatedArea.id())) {
//                // Update existing area
//                areaEntity = existingAreas.get(updatedArea.id());
//                areaEntity.setName(updatedArea.name());
//                areaEntity.setDescription(updatedArea.description());
//            } else {
//                // Check for duplicates by name within the same storage zone
//                Optional<StorageArea> existingAreaOpt = storageAreaRepository.findByStorageZoneIdAndName(storageZoneId, updatedArea.name());
//                if (existingAreaOpt.isPresent()) {
//                    throw new UniqueConstraintViolationException(
//                            MessageFormat.format("Duplicate area \"{0}\" in storage zone \"{1}\"", updatedArea.name(), existingZone.getName()));
//                }
//                // Create new area
//                areaEntity = new StorageArea(updatedArea, existingZone);
//            }
//            storageAreaRepository.save(areaEntity);
//
//            // Process locations
////            Map<Long, StorageLocation> existingLocations = areaEntity.getStorageLocations()
////                    .stream()
////                    .collect(Collectors.toMap(StorageLocation::getId, location -> location));
//

    /// /            for (StorageLocationRequestDTO updatedLocation : updatedArea.locations()) {
    /// /                StorageLocation locationEntity;
    /// /
    /// /                if (updatedLocation.storageLocationId() != null && existingLocations.containsKey(updatedLocation.storageLocationId())) {
    /// /                    // Update existing location
    /// /                    locationEntity = existingLocations.get(updatedLocation.storageLocationId());
    /// /                    locationEntity.setName(updatedLocation.locationName());
    /// /                    locationEntity.setDescription(updatedLocation.description());
    /// /                } else {
    /// /                    // Create new location
    /// /                    locationEntity = new StorageLocation(updatedLocation, areaEntity);
    /// /                }
    /// /                storageLocationRepository.save(locationEntity);
    /// /
    /// /                // Process levels
    /// /                Set<String> updatedLevels = new HashSet<>(updatedLocation.levels());
    /// /                Map<String, StorageLevel> existingLevels = locationEntity.getStorageLevels()
    /// /                        .stream()
    /// /                        .collect(Collectors.toMap(StorageLevel::getName, level -> level));
    /// /
    /// /                // Update or create levels
    /// /                for (String levelName : updatedLevels) {
    /// /                    if (!existingLevels.containsKey(levelName)) {
    /// /                        StorageLevel newLevel = new StorageLevel(levelName, locationEntity);
    /// /                        storageLevelRepository.save(newLevel);
    /// /                    }
    /// /                    existingLevels.remove(levelName);
    /// /                }
    /// /
    /// /                // Delete levels not in updated data (mock check included)
    /// /                for (StorageLevel level : existingLevels.values()) {
    /// /                    if (!mockCheckIfLevelHasItems(level)) {
    /// /                        storageLevelRepository.delete(level);
    /// /                    }
    /// /                }
    /// /            }
    /// /
    /// /            // Remove locations not in updated data
    /// /            existingLocations.keySet().removeAll(
    /// /                    updatedArea.locations().stream()
    /// /                            .map(StorageLocationRequestDTO::storageLocationId)
    /// /                            .filter(Objects::nonNull)
    /// /                            .collect(Collectors.toSet())
    /// /            );
    /// /            existingLocations.values().forEach(storageLocationRepository::delete);
//        }
//
//        // Remove areas not in updated data
//        existingAreas.keySet().removeAll(
//                updatedData.areas().stream()
//                        .map(StorageAreaUpdateDTO::id)
//                        .filter(Objects::nonNull)
//                        .collect(Collectors.toSet())
//        );
//        System.out.println("existingAreas " + existingAreas);
//
//        existingAreas.values().forEach(area -> {
//            StorageArea managedArea = storageAreaRepository.findById(area.getId())
//                    .orElseThrow(() -> new IllegalStateException("Area not found: " + area.getId()));
//            System.out.println("area.getId() " + area.getId() + " " + managedArea.getId());
//            storageAreaRepository.delete(managedArea);
//            storageAreaRepository.flush(); // Ensure the delete query is sent to the database
//
//        });
//        existingAreas.values().forEach(storageAreaRepository::delete);
//
//        return existingZone;
//    }
//
//    // Mock check for items under a level
//    private boolean mockCheckIfLevelHasItems(StorageLevel level) {
//        // Replace with actual logic to check if a level has items under it
//        return false;
//    }

    // Method to retrieve all zones
//    public List<StorageZone> getAllZones() {
//        return storageZoneRepository.findAll();
//    }

//    public boolean processQRCode(User user, String qrCode) {
//        // Retrieve storage levels to compare the QR code
//        //List<StorageLevel> storageLevels = levelRepository.findByQrCode(qrCode);
//        List<StorageLevel> storageLevels = levelRepository.findAllByQrCode(qrCode);
//        if (storageLevels.isEmpty()) {
//            return false; // No matching QR code found
//        }
//
//        // Assuming one valid result is required; handle the first matching storage level
//        for (StorageLevel storageLevel : storageLevels) {
//            if (isLocationQRCode(qrCode)) {
//                handleLocationQRCode(user, storageLevel);
//            } else if (isUserQRCode(user, qrCode)) {
//                handleUserQRCode(user, storageLevel);
//            }
//        }
//
//        return true;
//    }

//    private boolean isLocationQRCode(String qrCode) {
//        // Implement logic to identify location QR code
//        return qrCode.startsWith("L");  // For example, location QR codes start with "L"
//    }

//    private boolean isUserQRCode(User user, String qrCode) {
//        // Implement logic to identify user QR code and check if it matches user's info
//        return qrCode.equals(user.getUsername());  // Assuming QR code is the username
//    }

//    private void handleLocationQRCode(User user, StorageLevel storageLevel) {
//        // Process location QR code, associate it with user's level_id
//        user.setLevelId(storageLevel.getId());
//        userRepository.save(user);
//    }

//    private void handleUserQRCode(User user, StorageLevel storageLevel) {
//        // Process user QR code and assign location if applicable
//        user.setLevelId(storageLevel.getId());
//        userRepository.save(user);
//    }

//    public boolean processLocationQRCode(String qrCode) {
//        //List<StorageLevel> storageLevels = levelRepository.findByQrCode(qrCode);
//        List<StorageLevel> storageLevels = levelRepository.findAllByQrCode(qrCode);
//
//        if (storageLevels.isEmpty()) {
//            return false; // No matching QR code found
//        }
//
//        // Assuming you want to handle the first matching storage level
//        StorageLevel level = storageLevels.get(1);
//
//        return true;
//    }
//    public StorageLevelLocationDisplayDTO getLocationDisplayById(Long id) {
//        StorageLevel level = storageLevelRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found with ID: " + id));
//        return new StorageLevelLocationDisplayDTO(level);
//    }

//    public List<StorageLevelLocationDisplayDTO> getLocationDisplayByQrCode(String qrCode) {
//        List<StorageLevel> levels = levelRepository.findAllByQrCode(qrCode);
//
//        // If the list is empty, throw an exception
//        if (levels.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No locations found with QR code: " + qrCode);
//        }
//
//        return levels.stream()
//                .map(level -> {
//                    if (level != null) {
//                        return new StorageLevelLocationDisplayDTO((StorageLevel) level);
//                    } else if (level == null) {
//                        throw new IllegalArgumentException("Null level encountered in the list.");
//                    } else {
//                        throw new IllegalArgumentException("Unexpected type: " + level.getClass());
//                    }
//                })
//                .toList();
//
//    }

//    public StorageLevelInfoDTO processQrCode(String qrCode) throws ChangeSetPersister.NotFoundException {
//        return levelRepository.findAllByQrCode(qrCode)
//                .stream()
//                .map(storageLevel -> new StorageLevelInfoDTO(storageLevel.getId(), storageLevel.getLevelName()))
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("QR Code not found"));
//    }

//    public StorageLevelResponseDTO processLocationQR(String qrCode) {
//        //StorageLevel storageLevel = levelRepository.findSingleByQrCode(qrCode).orElseThrow(() -> new EntityNotFoundException("QR Code not found in storage_level table"));
//        String normalizedQrCode = qrCode.trim().toLowerCase();
//        StorageLevel storageLevel = levelRepository.findSingleByQrCode(normalizedQrCode)
//                .orElseThrow(() -> new EntityNotFoundException("QR Code not found in storage_level table"));
//        return new StorageLevelResponseDTO(storageLevel.getLevelId(), storageLevel.getLevelName());
//    }

//    //scenario 12 - works when there is only one record in the storage_level table for that user - this scenario need to be validated
//    public void changeLocation(StorageChangeLocationRequestDTO request) throws Exception {
//
//        // Fetch the currently logged-in user
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        var currentUser = userRepository.findByUsername(username);
//
//        if (currentUser == null) {
//            throw new RuntimeException("User not found");
//        }

    // Check if the user has an active QR code
//        Optional<StorageLevel> activeQrCode = levelRepository.findByCreatedByAndEnabledTrueAndQrCodeIsNotNull(currentUser);

//        if (activeQrCode.isEmpty()) {
//            throw new Exception("Your username should have an active QR code to complete this action.");
//        }
//
//        // If active QR code is present, proceed with location change logic
//        StorageLevel level = activeQrCode.get();

//    }

//    public void changeLocation2(String username, Long levelId) {
//        validateUserHasActiveQRCode(username, levelId);
//    }

//    public void validateUserHasActiveQRCode(String username, Long levelId) {
//        boolean hasActiveQRCode = storageLevelRepository.existsActiveQRCodeForUser(levelId, username);
//        if (!hasActiveQRCode) {
//            throw new IllegalStateException("Your username should have an active QR code to complete this action.");
//        }
//    }

}
