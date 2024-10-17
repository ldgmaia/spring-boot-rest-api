package com.example.api.domain.inventoryitems;

import com.example.api.domain.categories.Category;
import com.example.api.domain.itemcondition.ItemCondition;
import com.example.api.domain.itemstatus.ItemStatus;
import com.example.api.domain.locations.Location;
import com.example.api.domain.models.Model;
import com.example.api.domain.mpns.MPN;
import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "inventory_items")
@Entity(name = "InventoryItem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
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

    private String grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiving_items_id")
    private ReceivingItem receivingItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    private String post;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    private String rbid;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    private String type;
    private BigDecimal cost;

    public InventoryItem(InventoryItemRegisterDTO inventoryRegisterDTO, User currentUser) {
        this.category = inventoryRegisterDTO.category();
        this.model = inventoryRegisterDTO.model();
        this.mpn = inventoryRegisterDTO.mpn();
        this.itemCondition = inventoryRegisterDTO.itemCondition();
        this.itemStatus = inventoryRegisterDTO.itemStatus();
//        this.grade = inventoryRegisterDTO.grade();
        this.receivingItem = inventoryRegisterDTO.receivingItem();
        this.createdBy = currentUser;
        this.post = inventoryRegisterDTO.post();
        this.serialNumber = inventoryRegisterDTO.serialNumber();
        this.rbid = inventoryRegisterDTO.rbid();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.location = inventoryRegisterDTO.location();
        this.type = inventoryRegisterDTO.type();
        this.cost = inventoryRegisterDTO.cost();
    }
}
