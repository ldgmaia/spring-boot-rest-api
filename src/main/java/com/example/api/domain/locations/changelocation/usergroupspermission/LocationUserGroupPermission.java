package com.example.api.domain.locations.changelocation.usergroupspermission;

import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroup;
import com.example.api.domain.storage.storagearea.StorageArea;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "location_user_group_permissions")
@Entity(name = "LocationUserGroupPermissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class LocationUserGroupPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_user_groups_id", nullable = false)
    private LocationUserGroup locationUserGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location_area_id", nullable = false)
    private StorageArea fromLocationArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_area_id", nullable = false)
    private StorageArea toLocationArea;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public LocationUserGroupPermission(LocationUserGroupPermissionRegisterDTO data) {
        this.locationUserGroup = data.locationUserGroup();
        this.fromLocationArea = data.fromLocationArea();
        this.toLocationArea = data.toLocationArea();
    }
}
