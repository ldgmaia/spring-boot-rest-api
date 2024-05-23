package com.example.api.domain.categorycomponent;

import com.example.api.domain.categories.Category;
import jakarta.validation.constraints.NotNull;

public record CategoryComponentRegisterDTO
        (@NotNull
         Category childCategory,

         @NotNull
         Category parentCategory) {
}
