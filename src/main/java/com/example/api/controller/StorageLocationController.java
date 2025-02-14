package com.example.api.controller;

import com.example.api.domain.locations.changelocation.ChangeLocationRequestDTO;
import com.example.api.domain.locations.changelocation.ChangeLocationService;
import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroupInfoDTO;
import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroupRequestDTO;
import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermissionInfoDTO;
import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermissionRequestDTO;
import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUserInfoDTO;
import com.example.api.domain.storage.StorageRequestDTO;
import com.example.api.domain.storage.StorageService;
import com.example.api.domain.storage.StorageUpdateDTO;
import com.example.api.domain.storage.storagearea.StorageAreaMoveDTO;
import com.example.api.domain.storage.storagelevel.StorageLevelMoveDTO;
import com.example.api.domain.storage.storagelocation.StorageLocationMoveDTO;
import com.example.api.domain.storage.storagezone.StorageZoneInfoDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storage-location")
@SecurityRequirement(name = "bearer-key")
public class StorageLocationController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private ChangeLocationService changeLocationService;

    @Transactional
    @Modifying
    @PostMapping
    public ResponseEntity<StorageZoneInfoDTO> createStorage(@RequestBody StorageRequestDTO request) {
        var savedZone = storageService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedZone);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity updateStorage(@PathVariable Long id, @RequestBody StorageUpdateDTO request) {
        storageService.update(id, request);

        return ResponseEntity.ok("OK");
    }

    @GetMapping
    public ResponseEntity<List<StorageZoneInfoDTO>> getAllStorage() {
        List<StorageZoneInfoDTO> zones = storageService.getAllZonesInfo();
        return ResponseEntity.ok(zones);
    }

    @Transactional
    @PutMapping("/move-area")
    public ResponseEntity<Void> moveStorageArea(@RequestBody StorageAreaMoveDTO request) {
        storageService.moveStorageArea(request);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PutMapping("/move-location")
    public ResponseEntity<Void> moveStorageLocation(@RequestBody StorageLocationMoveDTO request) {
        storageService.moveStorageLocation(request);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PutMapping("/move-level")
    public ResponseEntity<Void> moveStorageLevel(@RequestBody StorageLevelMoveDTO request) {
        storageService.moveStorageLevel(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/permission/create-user-group")
    @Transactional
    public ResponseEntity createLocationUserGroup(@RequestBody @Valid LocationUserGroupRequestDTO data) {
        changeLocationService.createLocationUserGroup(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/permission/update-user-group/{id}")
    @Transactional
    public ResponseEntity updateLocationUserGroup(@PathVariable Long id, @Valid @RequestBody LocationUserGroupRequestDTO data) {
        changeLocationService.updateLocationUserGroup(id, data);

        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/permission/delete-user-group/{id}")
    @Transactional
    public ResponseEntity<Void> deleteLocationUserGroup(@PathVariable Long id) {
        changeLocationService.deleteLocationUserGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/permission/get-groups")
    public ResponseEntity<List<LocationUserGroupInfoDTO>> getAllGroups() {
        return ResponseEntity.ok(changeLocationService.getAllLocationUserGroups());
    }

    @GetMapping("/permission/get-users-by-group/{id}")
    public ResponseEntity<List<LocationUserGroupUserInfoDTO>> getAllUsersByGroup(@PathVariable Long id) {
        return ResponseEntity.ok(changeLocationService.getAllUsersByGroup(id));
    }

    @PostMapping("/create-permission")
    @Transactional
    public ResponseEntity<LocationUserGroupPermissionInfoDTO> createPermission(
            @RequestBody @Valid LocationUserGroupPermissionRequestDTO data) {
        changeLocationService.createPermission(data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-permission/{id}")
    @Transactional
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        changeLocationService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-permission-by-group/{id}")
    public ResponseEntity<List<LocationUserGroupPermissionInfoDTO>> getPermissionsByGroup(@PathVariable Long id) {
        return ResponseEntity.ok(changeLocationService.getPermissionsByGroup(id));
    }

    @PutMapping("/change-location")
    public ResponseEntity changeLocation(@RequestBody @Valid ChangeLocationRequestDTO data) {
        changeLocationService.changeLocation(data);
        return ResponseEntity.ok(data);
    }
}
