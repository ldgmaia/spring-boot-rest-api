package com.example.api.domain.locations.changelocation.usergroups;

import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "location_user_groups")
@Entity(name = "LocationUserGroup")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class LocationUserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "locationUserGroup", cascade = CascadeType.ALL)
    private List<LocationUserGroupUser> users = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public LocationUserGroup(LocationUserGroupRegisterDTO data) {
        this.name = data.name();
        this.description = data.description();
    }
}
