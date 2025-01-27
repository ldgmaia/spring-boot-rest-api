package com.example.api.domain.storage.storagezone;

import com.example.api.domain.storage.StorageRequestDTO;
import com.example.api.domain.storage.storagearea.StorageArea;
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

@Table(name = "storage_zones")
@Entity(name = "StorageZone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class StorageZone {
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

    @OneToMany(mappedBy = "storageZone", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StorageArea> areas = new ArrayList<>();

    public StorageZone(StorageRequestDTO data) {
        this.name = data.name();
        this.description = data.description();
        this.enabled = true;
    }
}
