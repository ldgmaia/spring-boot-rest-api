package com.example.api.domain.purchaseorderitems;

import com.example.api.domain.purchaseorders.PurchaseOrder;
import com.example.api.domain.receivingitems.ReceivingItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name = "purchase_order_items")
@Entity(name = "PurchaseOrderItem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Long quantityOrdered;
    private BigDecimal unitPrice;
    private BigDecimal total;
    private Long qboItemId;
    private Long qboPurchaseOrderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_orders_id")
    private PurchaseOrder purchaseOrder;

    @OneToMany(mappedBy = "purchaseOrderItem", orphanRemoval = true)
    private List<ReceivingItem> receivingItems = new ArrayList<>();

    public PurchaseOrderItem(PurchaseOrderItemRegisterDTO data) {
        this.name = data.name();
        this.description = data.description();
        this.quantityOrdered = data.quantity();
        this.unitPrice = data.unitPrice();
        this.total = data.total();
        this.qboItemId = data.qboItemId();
        this.qboPurchaseOrderItemId = data.qboPurchaseOrderItemId();
        this.purchaseOrder = data.purchaseOrder();
    }
}
