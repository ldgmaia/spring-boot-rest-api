package com.example.api.domain.storage.storagelocation;

import com.example.api.domain.storage.storagearea.StorageArea;
import com.example.api.domain.storage.storagelevel.StorageLevel;
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

@Table(name = "storage_locations")
@Entity(name = "StorageLocation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class StorageLocation {
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
    @JoinColumn(name = "storage_area_id")
    private StorageArea storageArea;

    @OneToMany(mappedBy = "storageLocation", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<StorageLevel> levels = new ArrayList<>();

    public StorageLocation(StorageLocationRequestDTO data, StorageArea area) {
        this.name = data.name();
        this.description = data.description();
        this.storageArea = area;
        this.enabled = true;
    }

    public StorageLocation(StorageLocationUpdateDTO data, StorageArea area) {
        this.name = data.name();
        this.description = data.description();
        this.storageArea = area;
        this.enabled = true;
    }
}
