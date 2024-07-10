package com.example.api.domain.mpns;

public record MPNInfoListDTO(
        Long id,
        String name,
        String modelName,
        String status,
        Boolean enabled,
        String createdBy,
        String approvedBy
) {
    public MPNInfoListDTO(Long id, String name, String modelName, String status, Boolean enabled, String createdBy, String approvedBy) {
        this.id = id;
        this.name = name;
        this.modelName = modelName;
        this.status = status;
        this.enabled = enabled;
        this.createdBy = createdBy;
        this.approvedBy = approvedBy;
    }
}
