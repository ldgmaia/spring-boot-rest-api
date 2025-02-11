package com.example.api.repositories;

import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationUserGroupPermissionRepository extends JpaRepository<LocationUserGroupPermission, Long> {
}
