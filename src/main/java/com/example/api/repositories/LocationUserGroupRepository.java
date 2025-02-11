package com.example.api.repositories;

import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationUserGroupRepository extends JpaRepository<LocationUserGroup, Long> {
}
