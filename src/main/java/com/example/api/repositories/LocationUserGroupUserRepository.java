package com.example.api.repositories;

import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationUserGroupUserRepository extends JpaRepository<LocationUserGroupUser, Long> {

    List<LocationUserGroupUser> findByGroupId(Long groupId);

    List<LocationUserGroupUser> findByUserId(Long userId);

    Optional<LocationUserGroupUser> findByGroupIdAndUserId(Long groupId, Long userId);

    boolean existsByGroupIdAndUserId(Long groupId, Long userId);

    void deleteByGroupIdAndUserId(Long groupId, Long userId);
}
