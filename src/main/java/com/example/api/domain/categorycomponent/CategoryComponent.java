package com.example.api.domain.categorycomponent;

import com.example.api.domain.categories.Category;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "category_components")
@Entity(name = "CategoryComponent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CategoryComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_category_id")
    private Category childCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    private Boolean enabled;

    public CategoryComponent(CategoryComponentRegisterDTO data) {
        this.childCategory = data.childCategory();
        this.parentCategory = data.parentCategory();
        this.enabled = true;
    }

    public void deactivate() {
        this.enabled = false;
    }
}
