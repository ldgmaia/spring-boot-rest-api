package com.example.api.domain.assessments;

import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.itemcondition.ItemCondition;
import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "assessments")
@Entity(name = "Assessment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dataLevel;
    private String section;
    private String area;
    private Boolean present = true;
    private String model;
    private String mpn;

    @Column(nullable = false)
    private Boolean pulled = false;

    private String status;

    private String post;

    @Column(name = "company_grade")
    private String companyGrade;

    @Column(name = "cosmetic_grade")
    private String cosmeticGrade;

    @Column(name = "functional_grade")
    private String functionalGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_condition_id")
    private ItemCondition itemCondition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_inventory_item_id")
    private InventoryItem parentInventoryItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiving_items_id")
    private ReceivingItem receivingItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @CreatedBy
    private User createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Assessment(AssessmentRegisterDTO data) {
        this.dataLevel = data.dataLevel();
        this.section = data.section();
        this.area = data.area();
        this.present = data.present();
        this.model = data.model();
        this.mpn = data.mpn();
        this.pulled = data.pulled();
        this.status = data.status();
        this.post = data.post();
        this.companyGrade = data.companyGrade();
        this.cosmeticGrade = data.cosmeticGrade();
        this.functionalGrade = data.functionalGrade();
        this.itemCondition = data.itemCondition();
        this.parentInventoryItem = data.parentInventoryItem();
        this.inventoryItem = data.inventoryItem();
        this.receivingItem = data.receivingItem();
    }
}
