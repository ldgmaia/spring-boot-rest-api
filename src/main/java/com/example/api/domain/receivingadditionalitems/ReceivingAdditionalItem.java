package com.example.api.domain.receivingadditionalitems;

import com.example.api.domain.receivings.Receiving;
import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "receiving_additional_items")
@Entity(name = "ReceivingAdditionalItem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ReceivingAdditionalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiving_id", nullable = false)
    private Receiving receivingId;

    private String description;

    @Column(name = "quantity_received")
    private Long quantityReceived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    public ReceivingAdditionalItem(ReceivingAdditionalItemRegisterDTO receivingAdditionalItemRegister) {
        this.receivingId = receivingAdditionalItemRegister.receiving();
        this.description = receivingAdditionalItemRegister.description();
        this.quantityReceived = receivingAdditionalItemRegister.quantityReceived();
        this.createdBy = receivingAdditionalItemRegister.createdBy();

    }

}