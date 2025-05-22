package com.example.api.domain.orders;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private Long orderItemId;
    private String lineItemKey;
    private String sku;
    private String name;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderItem(OrderItemRegisterDTO data) {
        this.order = data.order();
        this.orderItemId = data.orderItemId();
        this.lineItemKey = data.lineItemKey();
        this.sku = data.sku();
        this.name = data.name();
        this.imageUrl = data.imageUrl();
        this.quantity = data.quantity();
        this.unitPrice = data.unitPrice();
        this.taxAmount = data.taxAmount();
        this.shippingAmount = data.shippingAmount();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
