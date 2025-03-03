package com.example.api.domain.itemtransferlog;

import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.storage.storagelevel.StorageLevel;
import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "item_transfer_log")
@Entity(name = "ItemTransferLog")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class ItemTransferLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private InventoryItem inventoryItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_storage_level_id", nullable = false)
    private StorageLevel fromStorageLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_storage_level_id", nullable = false)
    private StorageLevel toStorageLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @CreatedBy()
    private User createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "transfer_status")
    @Enumerated(EnumType.STRING)
    private TransferStatus transferStatus;

    private String message;

    public ItemTransferLog(ItemTransferRegisterDTO data) {
        this.inventoryItem = data.inventoryItem();
        this.fromStorageLevel = data.fromStorageLevel();
        this.toStorageLevel = data.toStorageLevel();
        this.transferStatus = data.transferStatus();
        this.message = data.message();
    }
}
