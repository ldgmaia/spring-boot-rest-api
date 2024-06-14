package com.example.api.domain.fieldsvalues.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.fields.DataType;
import com.example.api.domain.fieldsvalues.FieldValueRequestDTO;
import com.example.api.repositories.FieldRepository;
import com.example.api.repositories.FieldValueRepository;
import com.example.api.repositories.ValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateFieldDataTypeForValue implements FieldValueValidator {

    @Autowired
    private FieldValueRepository fieldValueRepository;

    @Autowired
    private ValueRepository valueRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Override
    public void validate(FieldValueRequestDTO data) {

        var field = fieldRepository.getReferenceById(data.fieldId());
        DataType dataType = field.getDataType();
        String value = data.valueData();

        switch (dataType) {
            case BOOLEAN:
                if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                    throw new ValidationException("Value is not compatible with BOOLEAN data type");
                }
                break;
            case STRING:
            case OPENTEXT:
                // No additional validation needed for String and Opentext data types
                break;
            case INTEGER:
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new ValidationException("Value is not compatible with INTEGER data type");
                }
                break;
            case PERCENTAGE:
                try {
                    double percentageValue = Double.parseDouble(value);
                    if (percentageValue < 0 || percentageValue > 100) {
                        throw new ValidationException("Value is not a valid percentage");
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Value is not compatible with PERCENTAGE data type");
                }
                break;
            case DOUBLE:
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    throw new ValidationException("Value is not compatible with DOUBLE data type");
                }
                break;
            default:
                throw new ValidationException("Data type not supported: " + dataType);
        }

    }
}
