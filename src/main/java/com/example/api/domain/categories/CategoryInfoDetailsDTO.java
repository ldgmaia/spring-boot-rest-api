package com.example.api.domain.categories;

import java.util.List;

public record CategoryInfoDetailsDTO(
        Long id,
        String name,
        Boolean needsPost,
        Boolean needsSerialNumber,
        CategoryInfoDTO parentCategory,
        List<CategoryInfoDTO> components
//        CategoryGroupInfoDTO categoryGroup,


//        List<CategoryFieldsInfoDTO> categoryFields
) {
//    public CategoryComponentInfoDTO(Category parentCategory) {
//        this(
//                parentCategory.getId(),
//                parentCategory.getName(),
//                parentCategory.getNeedsPost(),
//                parentCategory.getNeedsSerialNumber(),
//                parentCategory.get
//        );
//    }
}
