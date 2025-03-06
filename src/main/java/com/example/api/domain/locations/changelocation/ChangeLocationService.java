package com.example.api.domain.locations.changelocation;


import com.example.api.domain.ValidationException;
import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.itemtransferlog.ItemTransferLog;
import com.example.api.domain.itemtransferlog.ItemTransferRegisterDTO;
import com.example.api.domain.itemtransferlog.TransferStatus;
import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroup;
import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroupInfoDTO;
import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroupRegisterDTO;
import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroupRequestDTO;
import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermission;
import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermissionInfoDTO;
import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermissionRegisterDTO;
import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermissionRequestDTO;
import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUser;
import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUserInfoDTO;
import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUserRegisterDTO;
import com.example.api.domain.storage.storagearea.StorageArea;
import com.example.api.domain.storage.storagelevel.StorageLevel;
import com.example.api.domain.users.User;
import com.example.api.repositories.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChangeLocationService {

    @Autowired
    private LocationUserGroupRepository locationUserGroupRepository;

    @Autowired
    private LocationUserGroupPermissionRepository locationUserGroupPermissionRepository;

    @Autowired
    private StorageAreaRepository storageAreaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationUserGroupUserRepository locationUserGroupUserRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private StorageLevelRepository storageLevelRepository;

    @Autowired
    private ItemTransferLogRepository itemTransferLogRepository;

    @Autowired
    private ItemStatusRepository itemStatusRepository;

    public LocationUserGroupInfoDTO createLocationUserGroup(LocationUserGroupRequestDTO data) {

        List<User> users = userRepository.findAllById(data.users());
        data.users().forEach(user -> {
            if (!userRepository.existsById(user)) {
                throw new ValidationException("One or more users not found.");
            }
        });

        // Create and save the user group
        LocationUserGroup userGroup = new LocationUserGroup(new LocationUserGroupRegisterDTO(data.name(), data.description()));
        LocationUserGroup savedGroup = locationUserGroupRepository.save(userGroup);

        // Associate users with the group
        List<LocationUserGroupUser> userAssociations = users.stream().map(user -> new LocationUserGroupUser(new LocationUserGroupUserRegisterDTO(savedGroup, user))).toList();
        locationUserGroupUserRepository.saveAll(userAssociations);
        return new LocationUserGroupInfoDTO(savedGroup);
    }

    public LocationUserGroupInfoDTO updateLocationUserGroup(Long id, LocationUserGroupRequestDTO data) {
        LocationUserGroup group = locationUserGroupRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User group not found"));

        group.setName(data.name());
        group.setDescription(data.description());

        // Remove previous user associations before adding new ones
        locationUserGroupUserRepository.deleteAllByLocationUserGroup(group);
        locationUserGroupUserRepository.flush();

        List<User> users = userRepository.findAllById(data.users());
        if (users.size() != data.users().size()) {
            throw new ValidationException("One or more users not found.");
        }

        // Associate users with the group
        List<LocationUserGroupUser> userAssociations = users.stream().map(user -> new LocationUserGroupUser(new LocationUserGroupUserRegisterDTO(group, user))).toList();
        locationUserGroupUserRepository.saveAll(userAssociations);

        return new LocationUserGroupInfoDTO(group);
    }

    public void deleteLocationUserGroup(Long id) {
        LocationUserGroup userGroup = locationUserGroupRepository.findById(id).orElseThrow(() -> new ValidationException("User group not found."));
        userGroup.setUsers(new ArrayList<>());
        locationUserGroupRepository.save(userGroup);
        locationUserGroupRepository.delete(userGroup);
    }

    public List<LocationUserGroupInfoDTO> getAllLocationUserGroups() {
        return locationUserGroupRepository.findAll().stream()
                .map(LocationUserGroupInfoDTO::new)
                .toList();
    }

    public List<LocationUserGroupUserInfoDTO> getAllUsersByGroup(Long id) {
        if (!locationUserGroupRepository.existsById(id)) {
            throw new ValidationException("User group not found");
        }

        return locationUserGroupUserRepository.findByLocationUserGroupId(id).stream()
                .map(LocationUserGroupUserInfoDTO::new)
                .toList();
    }

    public LocationUserGroupPermissionInfoDTO createPermission(LocationUserGroupPermissionRequestDTO request) {
        LocationUserGroup userGroup = locationUserGroupRepository.findById(request.locationUserGroupId()).orElseThrow(() -> new RuntimeException("User group not found"));
        StorageArea fromLocationArea = storageAreaRepository.findById(request.fromLocationAreaId()).orElseThrow(() -> new RuntimeException("From storageLevel not found"));
        StorageArea toLocationArea = storageAreaRepository.findById(request.toLocationAreaId()).orElseThrow(() -> new RuntimeException("To storageLevel not found"));
        LocationUserGroupPermission permission = new LocationUserGroupPermission(new LocationUserGroupPermissionRegisterDTO(userGroup, fromLocationArea, toLocationArea));
        LocationUserGroupPermission savedPermission = locationUserGroupPermissionRepository.save(permission);

        return new LocationUserGroupPermissionInfoDTO(savedPermission);
    }

    public void deletePermission(Long id) {
        if (!locationUserGroupPermissionRepository.existsById(id)) {
            throw new ValidationException("Permission not found");
        }
        locationUserGroupPermissionRepository.deleteById(id);
    }

    public List<LocationUserGroupPermissionInfoDTO> getPermissionsByGroup(Long id) {
        return locationUserGroupPermissionRepository.findByLocationUserGroupId(id).stream()
                .map(LocationUserGroupPermissionInfoDTO::new)
                .toList();
    }

    public Boolean checkPermission(@Valid CheckPermissionRequestDTO data) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByUsername(username);

        return locationUserGroupPermissionRepository.existsByLocationUserGroupIdAndFromLocationAreaIdAndToLocationAreaId(currentUser.getId(), data.fromLocationAreaId(), data.toLocationAreaId());
    }

    public void changeLocation(ChangeLocationRequestDTO data) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByUsername(username);

        if (data.toLocationLevelId() == null) throw new ValidationException("Destination location ID cannot be empty");

        storageLevelRepository.findById(data.toLocationLevelId()).orElseThrow(() -> new ValidationException("Destination location not found"));

        if (data.ids().isEmpty()) throw new ValidationException("No item IDs provided");

        List<InventoryItem> inventoryItemsToModify = inventoryItemRepository.findAllById(data.ids());

        if (inventoryItemsToModify.isEmpty()) throw new ValidationException("No items found for IDs: " + data.ids());
        if (inventoryItemsToModify.size() != data.ids().size())
            throw new ValidationException("One or more items not found for IDs: " + data.ids());

        for (InventoryItem item : inventoryItemsToModify) {

            if (item.getStorageLevel().getId().equals(data.toLocationLevelId())) {
                throw new ValidationException("Item ID: " + item.getId() + " is already in the destination location");
            }

            var validStatusList = itemStatusRepository.findByCanTransferTrue();

            validStatusList.stream()
                    .filter(status -> status.getId().equals(item.getItemStatus().getId()))
                    .findAny()
                    .orElseThrow(() -> new ValidationException("Serial number: " + item.getSerialNumber() + " is " + item.getItemStatus().getName() + " and can't be transferred"));

            var fromLocationLevelId = item.getStorageLevel().getId();
            var toLocationLevelId = data.toLocationLevelId();
            var fromLocationAreaId = item.getStorageLevel().getStorageLocation().getStorageArea().getId();
            var toLocationAreaId = storageLevelRepository.findById(data.toLocationLevelId()).get().getStorageLocation().getStorageArea().getId();

//            var toLocationAreaId = Optional.of(data.toLocationLevelId())
//                    .flatMap(storageLevelRepository::findById)
//                    .map(storageLevel -> storageLevel.getStorageLocation().getStorageArea().getId())
//                    .orElse(null);


            var myselfLocationAreaId = storageAreaRepository.findByName("myself").getId();
            var loggedUserLocationLevelId = currentUser.getStorageLevel().getId();
            var loggedUserLocationGroupIds = locationUserGroupUserRepository.findByUserId(currentUser.getId())
                    .stream()
                    .map(locationUserGroupUser -> locationUserGroupUser.getLocationUserGroup().getId())
                    .collect(Collectors.toList());

            var fromLocationAreaToCheck = Objects.equals(fromLocationLevelId, loggedUserLocationLevelId) ? myselfLocationAreaId : fromLocationAreaId;
            var toLocationAreaToCheck = Objects.equals(toLocationLevelId, loggedUserLocationLevelId) ? myselfLocationAreaId : toLocationAreaId;

            var fromLocationLevel = item.getStorageLevel();
            var toLocationLevel = storageLevelRepository.getReferenceById(data.toLocationLevelId());

            Boolean canTransfer = locationUserGroupPermissionRepository.existsByLocationUserGroupIdInAndFromLocationAreaIdAndToLocationAreaId(loggedUserLocationGroupIds, fromLocationAreaToCheck, toLocationAreaToCheck);

            if (!canTransfer) {
                //throw new ValidationException("You don't have permission to transfer items from/to location");

                var log = new ItemTransferLog(new ItemTransferRegisterDTO(
                        item,
                        fromLocationLevel,
                        toLocationLevel,
                        TransferStatus.FAILURE,
                        "No permission to transfer from location " + fromLocationLevel.getName() + " (" + fromLocationLevel.getStorageLocation().getStorageArea().getName() + ")" + " to location " + toLocationLevel.getName() + " (" + toLocationLevel.getStorageLocation().getStorageArea().getName() + ")"
                ));
                itemTransferLogRepository.save(log);
                throw new ValidationException("User: " + currentUser.getUsername() + ". You don't have permission to transfer items from " + storageAreaRepository.findById(fromLocationAreaToCheck).get().getName() + " to " + storageAreaRepository.findById(toLocationAreaToCheck).get().getName() + " location");
            }

            StorageLevel newStorageLevel = storageLevelRepository.findById(data.toLocationLevelId())
                    .orElseThrow(() -> new ValidationException("Storage level not found for ID: " + data.toLocationLevelId()));

            item.setStorageLevel(newStorageLevel);

            inventoryItemRepository.save(item);
            var log = new ItemTransferLog(new ItemTransferRegisterDTO(item, fromLocationLevel, toLocationLevel, TransferStatus.SUCCESS, "SUCCESS - Location Changed"));
            itemTransferLogRepository.save(log);
        }
    }
}
