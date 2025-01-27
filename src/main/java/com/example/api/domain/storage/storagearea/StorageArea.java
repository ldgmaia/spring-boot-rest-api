package com.example.api.domain.storage.storagearea;

import com.example.api.domain.storage.storagelocation.StorageLocation;
import com.example.api.domain.storage.storagezone.StorageZone;
import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "storage_areas")
@Entity(name = "StorageArea")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class StorageArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean enabled;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @CreatedBy
    private User createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_zone_id")
    private StorageZone storageZone;

    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StorageLocation> locations = new ArrayList<>();

    public StorageArea(StorageAreaRequestDTO data, StorageZone zone) {
        this.name = data.name();
        this.description = data.description();
        this.storageZone = zone;
        this.enabled = true;
    }

    public StorageArea(StorageAreaUpdateDTO data, StorageZone zone) {
        this.name = data.name();
        this.description = data.description();
        this.storageZone = zone;
        this.enabled = true;
    }
}
