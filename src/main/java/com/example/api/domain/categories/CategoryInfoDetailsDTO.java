package com.example.api.domain.categories;

public record CategoryInfoDetailsDTO(
        Long id,
        String name,
        Boolean needsPost,
        Boolean needsSerialNumber,
        CategoryInfoDTO parentCategory
//        CategoryGroupInfoDTO categoryGroup,

//        List<CategoryComponentInfoDTO> components,
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
