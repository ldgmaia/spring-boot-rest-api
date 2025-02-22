package com.example.api.domain.receivings;

import com.example.api.domain.carriers.Carrier;
import com.example.api.domain.purchaseorders.PurchaseOrder;
import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.receivingpictures.ReceivingPicture;
import com.example.api.domain.suppliers.Supplier;
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

@Table(name = "receivings")
@Entity(name = "Receiving")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Receiving {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_lading")
    private String trackingLading;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carriers_id", nullable = true)
    private Carrier carrier;

    @Enumerated(EnumType.STRING)
    private ReceivingType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suppliers_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = true)
    private PurchaseOrder purchaseOrder;

    private String parcels;
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @CreatedBy()
    private User createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "receiving", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReceivingItem> receivingItems = new ArrayList<>();

    @OneToMany(mappedBy = "receiving", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReceivingPicture> pictures = new ArrayList<>();

    public Receiving(ReceivingRegisterDTO data) {
        this.trackingLading = data.trackingLading();
        this.carrier = data.carrier();
        this.type = data.type();
        this.supplier = data.supplier();
        this.purchaseOrder = data.purchaseOrder();
        this.notes = data.notes();
    }
}
