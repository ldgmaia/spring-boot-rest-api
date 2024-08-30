package com.example.api.domain.purchaseorders;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "purchase_orders")
@Entity(name = "PurchaseOrder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private String poNumber;
    private String currency;
    private BigDecimal total;
    private Long qboId;
    private LocalDateTime qboCreatedAt;
    private LocalDateTime qboUpdatedAt;
    private Long suppliersId;
    private String receivingStatus;
    private String watchingPo;
    private LocalDateTime lastReceivedAt;

    public PurchaseOrder(PurchaseOrderRegisterDTO data) {
        this.status = data.status();
        this.poNumber = data.poNumber();
        this.currency = data.currency();
        this.total = data.total();
        this.qboId = data.qboId();
        this.qboCreatedAt = data.qboCreatedAt();
        this.qboUpdatedAt = data.qboUpdatedAt();
        this.suppliersId = data.suppliersId();
        this.receivingStatus = data.receivingStatus();
        this.watchingPo = data.watchingPo();
    }
}
