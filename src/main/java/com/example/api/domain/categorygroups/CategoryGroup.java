package com.example.api.domain.categorygroups;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "category_groups")
@Entity(name = "CategoryGroup")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CategoryGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean enabled;


    public CategoryGroup(CategoryGroupRegisterDTO data) {
        this.name = data.name();
        this.enabled = true;
    }

    public void updateInfo(CategoryGroupUpdateDTO data) {

        this.name = (data.name() != null) ? data.name() : this.name;
    }

    public void deactivate() {
        this.enabled = false;
    }
}
