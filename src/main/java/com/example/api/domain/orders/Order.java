package com.example.api.domain.orders;

import com.example.api.domain.customers.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "orders")
@Entity(name = "Order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private String orderNumber;
    private String orderKey;
    private LocalDateTime orderDate;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private LocalDateTime paymentDate;
    private LocalDateTime shipByDate;
    private String orderStatus;
    private BigDecimal orderTotal;
    private BigDecimal amountPaid;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private String paymentMethod;
    private String carrierCode;
    private String trackingNumber;
    private String confirmation;
    private String requestedShippingService;
    private String serviceCode;
    private String packageCode;
    private String customerNotes;
    private String internalNotes;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    //    @CreatedDate
    private LocalDateTime createdAt;

    //    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    public Order(OrderRegisterDTO data) {
        this.orderId = data.orderId();
        this.orderNumber = data.orderNumber();
        this.orderKey = data.orderKey();
        this.orderDate = data.orderDate();
        this.createDate = data.createDate();
        this.modifyDate = data.modifyDate();
        this.paymentDate = data.paymentDate();
        this.shipByDate = data.shipByDate();
        this.orderStatus = data.orderStatus();
        this.orderTotal = data.orderTotal();
        this.amountPaid = data.amountPaid();
        this.taxAmount = data.taxAmount();
        this.shippingAmount = data.shippingAmount();
        this.paymentMethod = data.paymentMethod();
        this.carrierCode = data.carrierCode();
        this.trackingNumber = data.trackingNumber();
        this.confirmation = data.confirmation();
        this.requestedShippingService = data.requestedShippingService();
        this.serviceCode = data.serviceCode();
        this.packageCode = data.packageCode();
        this.customerNotes = data.customerNotes();
        this.internalNotes = data.internalNotes();
        this.customer = data.customer();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
