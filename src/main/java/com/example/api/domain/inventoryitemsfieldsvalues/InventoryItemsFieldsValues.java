package com.example.api.domain.inventoryitemsfieldsvalues;

import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.inventoryitems.InventoryItem;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "inventory_items_fields_values")
@Entity(name = "InventoryItemsFieldsValues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class InventoryItemsFieldsValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_values_id")
    private FieldValue fieldValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public InventoryItemsFieldsValues(InventoryItemsFieldsValuesRegisterDTO data) {
        this.fieldValue = data.fieldValue();
        this.inventoryItem = data.inventoryItem();
    }
}
