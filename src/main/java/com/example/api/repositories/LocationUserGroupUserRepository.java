package com.example.api.repositories;

import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroup;
import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationUserGroupUserRepository extends JpaRepository<LocationUserGroupUser, Long> {

    List<LocationUserGroupUser> findByLocationUserGroupId(Long locationUserGroupId);

    void deleteAllByLocationUserGroup(LocationUserGroup locationUserGroup);

    List<LocationUserGroupUser> findByUserId(Long id);
}
