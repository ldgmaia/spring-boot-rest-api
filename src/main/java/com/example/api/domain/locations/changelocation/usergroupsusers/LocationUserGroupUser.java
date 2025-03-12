package com.example.api.domain.locations.changelocation.usergroupsusers;

import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroup;
import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "location_user_group_users")
@Entity(name = "LocationUserGroupUser")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class LocationUserGroupUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private LocationUserGroup locationUserGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public LocationUserGroupUser(LocationUserGroupUserRegisterDTO data) {
        this.locationUserGroup = data.locationUserGroup();
        this.user = data.user();
        this.locationUserGroup.getUsers().add(this);
    }
}
