package com.example.api.repositories;

import com.example.api.domain.locations.changelocation.usergroupspermission.LocationUserGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationUserGroupPermissionRepository extends JpaRepository<LocationUserGroupPermission, Long> {
    List<LocationUserGroupPermission> findByLocationUserGroupId(Long locationUserGroupId);

    Boolean existsByLocationUserGroupIdInAndFromLocationAreaIdAndToLocationAreaId(List<Long> userGroupIds, Long fromLocationAreaId, Long toLocationAreaId);

    Boolean existsByLocationUserGroupIdAndFromLocationAreaIdAndToLocationAreaId(Long userId, Long fromIid, Long toId);
}
