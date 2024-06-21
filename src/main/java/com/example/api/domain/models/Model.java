package com.example.api.domain.models;

import com.example.api.domain.categories.Category;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "models")
@Entity(name = "Model")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;
    private String identifier;
    private String status;
    private Boolean enabled;
    private Boolean needsMpn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_id")
    private Category category;


    public Model(ModelRegisterDTO data) {
        this.name = data.name();
        this.description = data.description();
        this.identifier = data.identifier();
        this.status = data.status();
        this.needsMpn = data.needsMpn();
        this.category = data.category();
        this.enabled = true;
    }

    public void deactivate() {
        this.enabled = false;
    }
}
