package com.example.api.domain.sectionareas;

import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.sectionareamodels.SectionAreaModel;
import com.example.api.domain.sections.Section;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Table(name = "section_areas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sections_id", "name"})
})
@Entity(name = "SectionArea")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SectionArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sections_id")
    private Section section;

    private Long areaOrder;
    private Boolean printOnLabel;
    private Boolean printAreaNameOnLabel;
    private Long orderOnLabel;
    private Boolean isCritical;

    @OneToMany(mappedBy = "sectionArea", orphanRemoval = true)
    private Set<SectionAreaModel> sectionAreaModels = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sectionArea", orphanRemoval = true)
    private List<InventoryItem> inventoryItems = new ArrayList<>();

    public SectionArea(SectionAreaRegisterDTO data) {
        this.name = data.name();
        this.section = data.section();
        this.areaOrder = data.areaOrder();
        this.printOnLabel = data.printOnLabel();
        this.printAreaNameOnLabel = data.printAreaNameOnLabel();
        this.orderOnLabel = data.orderOnLabel();
        this.isCritical = data.isCritical();
    }
}
