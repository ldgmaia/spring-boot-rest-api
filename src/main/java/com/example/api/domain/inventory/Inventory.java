package com.example.api.domain.inventory;

import com.example.api.domain.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    /*Add category*/
    @Column(name = "category_id")
    private @NotNull Long categoryId;

    @Column(name = "model_id")
    private @NotNull Long modelId;

    @Column(name = "mpn_id")
    private @NotNull Long mpnId;

    @Column(name = "condition_id")
    private @NotNull Long conditionId;

    @Column(name = "receiving_items_id")
    private @NotNull Long receivingItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "by_quantity")
    private Boolean byQuantity;

    //private Long locationID;

    private Long quantity;
    private String post;
    private String serialNumber;

    public Inventory(InventoryRegisterDTO inventoryRegisterDTO, User currentUser) {
        this.id = inventoryRegisterDTO.id();  // Assign the correct ID
        this.categoryId = inventoryRegisterDTO.categoryId();
        this.modelId = inventoryRegisterDTO.modelId();
        this.mpnId = inventoryRegisterDTO.mpnId();
        this.conditionId = inventoryRegisterDTO.conditionId();
        this.receivingItemId = inventoryRegisterDTO.receivingItemId();
        this.createdBy = currentUser;
        this.byQuantity = inventoryRegisterDTO.byQuantity();
        this.quantity = inventoryRegisterDTO.quantity();
        this.post = inventoryRegisterDTO.post();
        this.serialNumber = inventoryRegisterDTO.serialNumber();
    }

}