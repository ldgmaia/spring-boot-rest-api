package com.example.api.domain.receivings;

import com.example.api.domain.carriers.Carrier;
import com.example.api.domain.suppliers.Supplier;
import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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

    private Long identifierId; // change this field to identifier_id and use the ID instead
    private String parcels;
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Receiving(ReceivingRegisterDTO data, User currentUser) {
        this.trackingLading = data.trackingLading();
        this.carrier = data.carrier();
        this.type = data.type();
        this.supplier = data.supplier();
        this.identifierId = data.identifierId();
        this.notes = data.notes();
        this.createdBy = currentUser;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
