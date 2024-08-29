package com.example.api.domain.purchaseorderitems;

import com.example.api.domain.purchaseorders.PurchaseOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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
    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
    private Long qboItemId;
    private Long qboPurchaseOrderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_orders_id")
    private PurchaseOrder purchaseOrder;

    public PurchaseOrderItem(PurchaseOrderItemRegisterDTO data) {
        this.name = data.name();
        this.description = data.description();
        this.quantity = data.quantity();
        this.unitPrice = data.unitPrice();
        this.total = data.total();
        this.qboItemId = data.qboItemId();
        this.qboPurchaseOrderItemId = data.qboPurchaseOrderItemId();
        this.purchaseOrder = data.purchaseOrder();
    }
}
