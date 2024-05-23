package com.example.api.domain.categories;

import com.example.api.domain.categorygroups.CategoryGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table(name = "categories")
@Entity(name = "Category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    private Boolean enabled;
    private Boolean needs_post;
    private Boolean needs_serial_number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_group_id")
    private CategoryGroup categoryGroup;

    public Category(CategoryRegisterDTO data) {
        this.name = data.name();
        this.enabled = true;
        this.needs_post = data.needsPost();
        this.needs_serial_number = data.needsSerialNumber();
        this.categoryGroup = data.categoryGroup();

    }

    public void deactivate() {
        this.enabled = false;
    }
}
