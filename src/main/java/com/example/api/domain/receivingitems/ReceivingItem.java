package com.example.api.domain.receivingitems;

import com.example.api.domain.purchaseorderitems.PurchaseOrderItem;
import com.example.api.domain.receivings.Receiving;
import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "receiving_items")
@Entity(name = "ReceivingItem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
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

    @Column(name = "quantity_received")
    private Long quantityReceived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "receivable_item")
    private Boolean receivableItem;

    @Column(name = "additional_item", nullable = true)
    private Boolean additionalItem;

    public ReceivingItem(ReceivingItemRegisterDTO receivingItemRegister) {
        this.receiving = receivingItemRegister.receiving();
        this.purchaseOrderItem = receivingItemRegister.purchaseOrderItem();
        this.description = receivingItemRegister.description();
        this.quantityToReceive = receivingItemRegister.quantityToReceive();
        this.quantityReceived = receivingItemRegister.quantityReceived();
        this.createdBy = receivingItemRegister.createdBy();
        this.receivableItem = receivingItemRegister.receivableItem();
        this.additionalItem = receivingItemRegister.additionalItem();
    }
}
