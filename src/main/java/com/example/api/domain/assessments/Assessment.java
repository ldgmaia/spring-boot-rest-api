//package com.example.api.domain.assessments;
//
//import com.example.api.domain.inventoryitems.InventoryItem;
//import com.example.api.domain.purchaseorderitems.PurchaseOrderItem;
//import com.example.api.domain.receivings.Receiving;
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@EqualsAndHashCode(of = "id")
//public class Assessment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "purchase_order_item_id", nullable = false)
//    private PurchaseOrderItem purchaseOrderItem;
//
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "receiving_id", nullable = false)
//    private Receiving receiving;
//
//    private String description;
//    private String status;
//
//    private Long quantity;
//
//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    private LocalDateTime updatedAt;
//
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "tracking_lading", nullable = false)
//    private Receiving trackingLading;
//
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "serial_number", nullable = false)
//    private InventoryItem serialNumber;
//
//}
