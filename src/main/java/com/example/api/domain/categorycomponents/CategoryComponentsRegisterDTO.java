package com.example.api.domain.categorycomponents;

import com.example.api.domain.categories.Category;
import jakarta.validation.constraints.NotNull;

public record CategoryComponentsRegisterDTO
        (@NotNull
         Category childCategory,

         @NotNull
         Category parentCategory) {
}
