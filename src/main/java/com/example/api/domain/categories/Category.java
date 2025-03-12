package com.example.api.domain.categories;

import com.example.api.domain.categorycomponents.CategoryComponents;
import com.example.api.domain.categoryfields.CategoryFields;
import com.example.api.domain.categorygroups.CategoryGroups;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Table(name = "categories")
@Entity(name = "Category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    private Boolean enabled;
    private Boolean needsPost;
    private Boolean needsSerialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_group_id")
    private CategoryGroups categoryGroup;

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private List<CategoryFields> categoryFields = new ArrayList<>();

    @OneToMany(mappedBy = "parentCategory", orphanRemoval = true)
    private List<CategoryComponents> parentCategory = new ArrayList<>();

    public Category(CategoryRegisterDTO data) {
        this.name = data.name();
        this.enabled = true;
        this.needsPost = data.needsPost();
        this.needsSerialNumber = data.needsSerialNumber();
        this.categoryGroup = data.categoryGroup();
    }

    public void deactivate() {
        this.enabled = false;
    }
}
