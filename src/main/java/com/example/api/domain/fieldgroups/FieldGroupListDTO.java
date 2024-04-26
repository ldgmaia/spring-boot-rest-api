package com.example.api.domain.fieldgroups;

public record FieldGroupListDTO(Long id, String name, Boolean enabled) {

    public FieldGroupListDTO(FieldGroup fieldGroup) {
        this(fieldGroup.getId(), fieldGroup.getName(), fieldGroup.getEnabled());
    }


}
