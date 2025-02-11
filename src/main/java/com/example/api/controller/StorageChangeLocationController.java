package com.example.api.controller;

import com.example.api.domain.locations.changelocation.ChangeLocationService;
import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroupInfoDTO;
import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroupRequestDTO;
import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermissionInfoDTO;
import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermissionRequestDTO;
import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUser;
import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUserInfoDTO;
import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUserRequestDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
@SecurityRequirement(name = "bearer-key")
public class StorageChangeLocationController {

    @Autowired
    private ChangeLocationService locationMainService;

    @PostMapping("/create-user-group")
    @Transactional
    public ResponseEntity<LocationUserGroupInfoDTO> createGroup(@RequestBody @Valid LocationUserGroupRequestDTO data) {
        return ResponseEntity.ok(locationMainService.createLocationUserGroup(data));
    }

    @GetMapping("/get-groups")
    public ResponseEntity<List<LocationUserGroupInfoDTO>> getAllGroups() {
        return ResponseEntity.ok(locationMainService.getAllLocationUserGroups());
    }

    @PostMapping("/add-user-to-group")
    public ResponseEntity addUserToGroup(@RequestBody @Valid LocationUserGroupUserRequestDTO data) {
        LocationUserGroupUser usertogroup = locationMainService.addUserToGroup(data.groupId(), data.userId());
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/get-all-users-by-group-id/{id}")
    public ResponseEntity<List<LocationUserGroupUserInfoDTO>> getAllUsersByGroupId(@PathVariable Long id) {
        var LocationUserGroupUser = locationMainService.getAllUsersByGroupId(id);
        return ResponseEntity.ok(LocationUserGroupUser);
    }

    @PostMapping("/create-permission")
    public ResponseEntity<LocationUserGroupPermissionInfoDTO> createPermission(@RequestBody @Valid LocationUserGroupPermissionRequestDTO data) {
        return ResponseEntity.ok(locationMainService.createPermission(data));
    }

    @GetMapping("/get-all-permission-groups")
    public ResponseEntity<List<LocationUserGroupPermissionInfoDTO>> getAllPermissions() {
        return ResponseEntity.ok(locationMainService.getAllGroupPermissions());
    }

    @GetMapping("/get-permission-group-by-id/{id}")
    public ResponseEntity<List<LocationUserGroupPermissionInfoDTO>> getAllUserGroupPermissionsByGroupId(@PathVariable Long id) {
        var usersInUserGroup = locationMainService.getAllUserGroupPermissionsByGroupId(id);
        return ResponseEntity.ok(usersInUserGroup);
    }

}
