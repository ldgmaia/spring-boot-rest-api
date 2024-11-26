package com.example.api.domain.receivingitems;

import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.purchaseorderitems.PurchaseOrderItem;
import com.example.api.domain.receivings.Receiving;
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

@Table(name = "receiving_items")
@Entity(name = "ReceivingItem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class ReceivingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiving_id", nullable = false)
    private Receiving receiving;

    @ManyToOne
    @JoinColumn(name = "purchase_order_item_id", nullable = true)
    private PurchaseOrderItem purchaseOrderItem;

    private String description;

    @Column(name = "quantity_to_receive", nullable = true)
    private Long quantityToReceive;

    @Column(name = "quantity_already_received")
    private Long quantityAlreadyReceived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @CreatedBy()
    private User createdBy;

    private String status;

    @Column(name = "receivable_item")
    private Boolean receivableItem;

    @Column(name = "additional_item", nullable = true)
    private Boolean additionalItem;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "receivingItem", orphanRemoval = true)
    private List<InventoryItem> inventoryItems = new ArrayList<>();

    public ReceivingItem(ReceivingItemRegisterDTO receivingItemRegister) {
        this.receiving = receivingItemRegister.receiving();
        this.purchaseOrderItem = receivingItemRegister.purchaseOrderItem();
        this.description = receivingItemRegister.description();
        this.quantityToReceive = receivingItemRegister.quantityToReceive();
        this.quantityAlreadyReceived = receivingItemRegister.quantity();
        this.status = "Pending Items";
        this.receivableItem = receivingItemRegister.receivableItem();
        this.additionalItem = receivingItemRegister.additionalItem();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
