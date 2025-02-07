//package com.example.api.domain.itemtransfer;
//
//import com.example.api.domain.inventoryitems.InventoryItem;
//import com.example.api.domain.users.User;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.time.LocalDateTime;
//
//
//@Entity(name = "ItemTransferLog")
//@Table(name = "log_transfer_items")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
//public class ItemTransferLog {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "item_id", nullable = false)
//    private InventoryItem item;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sender_id", nullable = false)
//    private User initiator;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "receptor_id", nullable = false)
//    private User receiver;
//
//    @Column(name = "action_timestamp", nullable = false)
//    private LocalDateTime transferDate;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "created_by", nullable = false)
//    private User createdBy;
//
//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    private LocalDateTime updatedAt;
//
//    public enum ConfirmationStatus {
//        SUCCESS, FAILED
//    }
//
//    public ItemTransferLog (InventoryItem item, User initiator, User receiver, LocalDateTime transferDate) {
//        this.item = item;
//        this.initiator = initiator;
//        this.receiver = receiver;
//        this.transferDate = transferDate;
//    }
//
//}
