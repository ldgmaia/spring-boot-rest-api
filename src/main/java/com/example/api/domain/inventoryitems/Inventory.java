package com.example.api.domain.inventoryitems;

import com.example.api.domain.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Table(name = "inventory_items")
@Entity(name = "Inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private @NotNull Long categoryId; // should use the entity

    @Column(name = "model_id")
    private @NotNull Long modelId; // should use the entity

    @Column(name = "mpn_id")
    private @NotNull Long mpnId; // should use the entity

    @Column(name = "item_conditions_id")
    private @NotNull Long itemConditionsId; // once we have the entity for Conditions, update this to use the entity instead of the ID

    @Column(name = "receiving_items_id")
    private @NotNull Long receivingItemId; // should use the entity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    //    @Column(name = "created_at", nullable = false, updatable = false)
    //    @CreationTimestamp
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Long locationsId; // once we have the entity for Locations, update this to use the entity instead of the ID

    private String rbid;
    private String post;
    private String serialNumber;

    public Inventory(InventoryRegisterDTO inventoryRegisterDTO, User currentUser) {
        this.categoryId = inventoryRegisterDTO.categoryId();
        this.modelId = inventoryRegisterDTO.modelId();
        this.mpnId = inventoryRegisterDTO.mpnId();
        this.itemConditionsId = inventoryRegisterDTO.itemConditionsId();
        this.receivingItemId = inventoryRegisterDTO.receivingItemId();
        this.createdBy = currentUser;
        this.post = inventoryRegisterDTO.post();
        this.serialNumber = inventoryRegisterDTO.serialNumber();
        this.locationsId = inventoryRegisterDTO.locationId();
        this.rbid = inventoryRegisterDTO.rbid();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
