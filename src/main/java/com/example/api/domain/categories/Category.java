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
    private Boolean needsPost;
    private Boolean needsSerialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_group_id")
    private CategoryGroup categoryGroup;

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
