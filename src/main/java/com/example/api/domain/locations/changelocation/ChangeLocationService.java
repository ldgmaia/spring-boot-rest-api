package com.example.api.domain.locations.changelocation;


import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroup;
import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroupInfoDTO;
import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroupRequestDTO;
import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermission;
import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermissionInfoDTO;
import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermissionRequestDTO;
import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUser;
import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUserInfoDTO;
import com.example.api.domain.storage.storagearea.StorageArea;
import com.example.api.domain.users.User;
import com.example.api.repositories.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ChangeLocationService {

    @Autowired
    private LocationUserGroupRepository locationUserGroupRepository;

    @Autowired
    private LocationUserGroupUserRepository locationUserGroupUserRepository;

    @Autowired
    private LocationUserGroupPermissionRepository locationUserGroupPermissionRepository;

    @Autowired
    private StorageAreaRepository storageAreaRepository;

    @Autowired
    private UserRepository userRepository;

    public LocationUserGroupInfoDTO createLocationUserGroup(LocationUserGroupRequestDTO data) {
        LocationUserGroup group = new LocationUserGroup(data.name(), data.description());
        return new LocationUserGroupInfoDTO(locationUserGroupRepository.save(group));
    }

    public List<LocationUserGroupInfoDTO> getAllLocationUserGroups() {
        return locationUserGroupRepository.findAll().stream().map(LocationUserGroupInfoDTO::new).toList();
    }

    // CRUD for Location User Group Users
    public LocationUserGroupUser addUserToGroup(Long groupId, Long userId) {

        LocationUserGroup locationUserGroup = locationUserGroupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Verificar si el usuario ya está en el grupo
        if (locationUserGroupUserRepository.existsByGroupIdAndUserId(groupId, userId)) {
            throw new RuntimeException("User is already in the group");
        }

        // Guardar la relación entre el usuario y el grupo usando un constructor
        LocationUserGroupUser locationUserGroupUser = new LocationUserGroupUser(locationUserGroup, user);

        locationUserGroupUserRepository.save(locationUserGroupUser);
        return locationUserGroupUser;
    }

    public List<LocationUserGroupInfoDTO> getAllUsersInGroups() {
        return locationUserGroupUserRepository.findAll().stream().map(LocationUserGroupInfoDTO::new).toList();
    }

    public List<LocationUserGroupUserInfoDTO> getAllUsersByGroupId(Long groupId) {
        return Collections.singletonList(new LocationUserGroupUserInfoDTO(locationUserGroupUserRepository.getReferenceById(groupId)));
    }

    // CRUD for Location User Group Permissions
    public LocationUserGroupPermissionInfoDTO createPermission(LocationUserGroupPermissionRequestDTO data) {
        LocationUserGroup group = locationUserGroupRepository.findById(data.locationUserGroupId()).orElseThrow(() -> new RuntimeException("Group not found"));
        StorageArea fromLocationArea = storageAreaRepository.findById(data.fromLocationAreaId()).orElseThrow(() -> new RuntimeException("From location not found"));
        StorageArea toLocationArea = storageAreaRepository.findById(data.toLocationAreaId()).orElseThrow(() -> new RuntimeException("To location not found"));

        LocationUserGroupPermission permission = new LocationUserGroupPermission(group, fromLocationArea, toLocationArea, data.name(), data.description());
        return new LocationUserGroupPermissionInfoDTO(locationUserGroupPermissionRepository.save(permission));
    }

    public List<LocationUserGroupPermissionInfoDTO> getAllGroupPermissions() {
        return locationUserGroupPermissionRepository.findAll().stream().map(LocationUserGroupPermissionInfoDTO::new).toList();
    }

    public List<LocationUserGroupPermissionInfoDTO> getAllUserGroupPermissionsByGroupId(Long groupId) {
        return locationUserGroupPermissionRepository.findById(groupId).stream().map(LocationUserGroupPermissionInfoDTO::new).toList();
    }

    public void changeLocation(@Valid ChangeLocationRequestDTO data) {
        System.out.println("data " + data);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByUsername(username);
        System.out.println("currentUser " + currentUser.getUsername() + " - " + currentUser.getId());
    }
}
