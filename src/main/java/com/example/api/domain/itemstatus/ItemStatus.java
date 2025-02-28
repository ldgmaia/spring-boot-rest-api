package com.example.api.domain.itemstatus;

import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "item_statuses")
@Entity(name = "ItemStatus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "can_transfer", nullable = false)
    private Boolean canTransfer;

    public ItemStatus(String name, User createdBy, Boolean canTransfer) {
        this.name = name;
        this.createdBy = createdBy;
        this.canTransfer = canTransfer;
    }
}
