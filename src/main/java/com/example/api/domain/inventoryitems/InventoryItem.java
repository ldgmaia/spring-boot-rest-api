package com.example.api.domain.inventoryitems;

import com.example.api.domain.categories.Category;
import com.example.api.domain.inventoryitemscomponents.InventoryItemComponents;
import com.example.api.domain.inventoryitemsfieldsvalues.InventoryItemsFieldsValues;
import com.example.api.domain.itemcondition.ItemCondition;
import com.example.api.domain.itemstatus.ItemStatus;
import com.example.api.domain.models.Model;
import com.example.api.domain.mpns.MPN;
import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.sectionareas.SectionArea;
import com.example.api.domain.storage.storagelevel.StorageLevel;
import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "inventory_items")
@Entity(name = "InventoryItem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mpn_id", nullable = true)
    private MPN mpn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_conditions_id")
    private ItemCondition itemCondition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_statuses_id")
    private ItemStatus itemStatus;

    private String companyGrade;
    private String functionalGrade;
    private String cosmeticGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiving_items_id")
    private ReceivingItem receivingItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_area_id")
    private SectionArea sectionArea;

    private Boolean present;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @CreatedBy()
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspected_by")
    private User inspectedBy;

    private String post;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    private String rbid;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedDate
    private LocalDateTime lastInspectedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_level_id")
    private StorageLevel storageLevel;

    private String type;
    private BigDecimal cost;

    @OneToMany(mappedBy = "parentInventoryItem", orphanRemoval = true)
    private List<InventoryItemComponents> inventoryItemComponents = new ArrayList<>();

    @OneToMany(mappedBy = "inventoryItem", orphanRemoval = true)
    private List<InventoryItemsFieldsValues> inventoryItemsFieldsValues = new ArrayList<>();

    public InventoryItem(InventoryItemRegisterDTO inventory) {
        this.category = inventory.category();
        this.model = inventory.model();
        this.mpn = inventory.mpn();
        this.itemCondition = inventory.itemCondition();
        this.itemStatus = inventory.itemStatus();
        this.receivingItem = inventory.receivingItem();
        this.post = inventory.post();
        this.present = inventory.present();
        this.sectionArea = inventory.sectionArea();
        this.serialNumber = inventory.serialNumber();
        this.rbid = inventory.rbid();
        this.storageLevel = inventory.storageLevel();
        this.type = inventory.type();
        this.cost = inventory.cost();
        this.companyGrade = inventory.companyGrade();
    }
}
