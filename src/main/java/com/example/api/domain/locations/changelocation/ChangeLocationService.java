package com.example.api.domain.locations.changelocation;


import com.example.api.domain.ValidationException;
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
        // Fetch the user group
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

    public void changeLocation(ChangeLocationRequestDTO data) {
//        System.out.println("data " + data);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByUsername(username);
        System.out.println("currentUser " + currentUser.getUsername() + " - " + currentUser.getId());
    }

    public Boolean checkPermission(@Valid CheckPermissionRequestDTO data) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByUsername(username);

        return locationUserGroupPermissionRepository.existsByLocationUserGroupIdAndFromLocationAreaIdAndToLocationAreaId(currentUser.getId(), data.fromLocationAreaId(), data.toLocationAreaId());
    }
}
