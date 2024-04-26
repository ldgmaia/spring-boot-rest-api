package com.example.api.domain.fieldgroups;

public record FieldGroupInfoDTO(Long id, String name, Boolean enabled) {

    public FieldGroupInfoDTO(FieldGroup fieldGroup) {
        this(fieldGroup.getId(), fieldGroup.getName(), fieldGroup.getEnabled());
    }
}
