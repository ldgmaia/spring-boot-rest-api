package com.example.api.domain.sections;

import com.example.api.domain.models.Model;
import com.example.api.domain.sectionareas.SectionArea;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "sections", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"models_id", "name"})
})
@Entity(name = "Section")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Long sectionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "models_id")
    private Model model;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SectionArea> areas = new ArrayList<>();

    public Section(SectionRegisterDTO data) {
        this.name = data.name();
        this.sectionOrder = data.sectionOrder();
        this.model = data.model();
    }
}
