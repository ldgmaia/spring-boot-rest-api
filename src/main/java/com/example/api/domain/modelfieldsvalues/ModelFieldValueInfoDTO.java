package com.example.api.domain.modelfieldsvalues;

public record ModelFieldValueInfoDTO(
        Long id,
        Long fieldId,
        String fieldName,
        Long valueDataId,
        String valueData
) {
//    public ModelFieldValueInfoDTO(Model model) {
//        this(
//                model.getId(),
//                model.getName(),
//                model.getDescription(),
//                model.getIdentifier(),
//                model.getEnabled(),
//                model.getNeedsMpn(),
//                new CategoryInfoDTO(model.getCategory())
//
////                field.getFieldGroup() != null ? new FieldGroupInfoDTO(field.getFieldGroup()) : null); // another way of doing the same check
////                Optional.ofNullable(field.getFieldGroup()).map(FieldGroupInfoDTO::new).orElse(null));
//        );
//    }
}


