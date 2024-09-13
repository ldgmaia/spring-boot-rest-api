package com.example.api.domain.receivings;

import com.example.api.domain.suppliers.Supplier;
import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "receivings")
@Entity(name = "Receiving")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Receiving {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackingLading;
    private Carriers carrier;
    private ReceivingType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suppliers_id")
    private Supplier supplierId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "purchase_order_id")
//    private Long purchaseOrder;

    private String identifier;
    private String parcels;
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    public Receiving(ReceivingRegisterDTO data, User currentUser) {
        this.trackingLading = data.trackingLading();
        this.carrier = data.carrier();
        this.type = data.type();
        this.supplierId = data.supplierId();
        this.identifier = data.identifier();
        this.notes = data.notes();
        this.createdBy = currentUser;

    }
}
