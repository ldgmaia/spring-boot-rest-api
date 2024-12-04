package com.example.api.domain.inventoryitemscomponents;

import com.example.api.domain.inventoryitems.InventoryItem;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "inventory_items_components")
@Entity(name = "InventoryItemComponent")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class InventoryItemComponents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_inventory_item_id", nullable = true)
    private InventoryItem parentInventoryItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public InventoryItemComponents(InventoryItemComponentRegisterDTO component) {
        this.parentInventoryItem = component.parentInventoryItem();
        this.inventoryItem = component.inventoryItem();
    }
}
