package com.example.api.domain.models;

public class ModelFieldId {
    private final Long fieldId;
    private final Long valueDataId;

    public ModelFieldId(Long fieldId, Long valueDataId) {
        this.fieldId = fieldId;
        this.valueDataId = valueDataId;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelFieldId that = (ModelFieldId) o;

        if (!fieldId.equals(that.fieldId)) return false;
        return valueDataId.equals(that.valueDataId);
    }

    @Override
    public int hashCode() {
        int result = fieldId.hashCode();
        result = 31 * result + valueDataId.hashCode();
        return result;
    }
}
