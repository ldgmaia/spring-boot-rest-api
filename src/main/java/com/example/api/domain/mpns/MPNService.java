package com.example.api.domain.mpns;

import com.example.api.domain.ValidationException;
import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.fieldsvalues.FieldValueRegisterDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldValueInfoDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldValueRegisterDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldValueRequestDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldsValues;
import com.example.api.domain.values.Value;
import com.example.api.domain.values.ValueInfoDTO;
import com.example.api.domain.values.ValueRegisterDTO;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MPNService {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private FieldValueRepository fieldValueRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private MPNFieldValueRepository mpnFieldValueRepository;

    @Autowired
    private MPNRepository mpnRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValueRepository valueRepository;

//    @Autowired
//    private List<ReceivingValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public MPNInfoDTO register(MPNRequestDTO data) {

        // Fetch the currently logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByUsername(username);

        var mpn = new MPN(new MPNRegisterDTO(data.name(), data.description(), data.status(), modelRepository.getReferenceById(data.modelId())), currentUser);
        mpnRepository.save(mpn);


        if (data.mpnFieldsValues() != null) {
            data.mpnFieldsValues().forEach(mfv -> {

                var field = fieldRepository.getReferenceById(mfv.fieldId());

                Value valueData;

                // Case 1: Both valueData and valueDataId are null
                if (mfv.valueDataId() == null && (mfv.valueData() == null || mfv.valueData().isEmpty())) {
                    throw new ValidationException("Value is required for " + mfv);
                }

                // Case 2: valueDataId exists
                if (mfv.valueDataId() != null) {
                    valueData = valueRepository.getReferenceById(mfv.valueDataId());
                } else {
                    // Case 3: valueData provided, valueDataId must be null
                    if (valueRepository.existsByValueData(mfv.valueData())) {
                        valueData = valueRepository.findByValueData(mfv.valueData());
                    } else {
                        valueData = valueRepository.save(new Value(new ValueRegisterDTO(mfv.valueData())));
                    }
                }

                // check if field value already exists
                FieldValue fieldValue;
                if (fieldValueRepository.existsByValuesDataIdAndFieldsId(valueData.getId(), field.getId())) {
                    fieldValue = fieldValueRepository.findByFieldIdAndValueDataId(field.getId(), valueData.getId());
                } else {
                    var newFieldvalue = new FieldValue(new FieldValueRegisterDTO(valueData, (double) 0, field));
                    fieldValue = fieldValueRepository.save(newFieldvalue);
                }

//                var fieldValue = fieldValueRepository.findByFieldIdAndValueDataId(mfv.fieldId(), mfv.valueDataId());

                var mpnFieldValue = new MPNFieldsValues(new MPNFieldValueRegisterDTO(fieldValue, mpn));
                mpnFieldValueRepository.save(mpnFieldValue);
            });
        }
//        return new MPNInfoDTO(mpn, mpnFieldsValues != null ? mpnFieldsValues : List.of());
        return new MPNInfoDTO(mpn);
    }

    public List<MPNFieldsValuesDTO> listMpnFields(Long modelId) {
        var fieldList = mpnRepository.findMPNFieldsByModelId(modelId);

        return fieldList.stream()
                .map(f -> {
                    List<ValueInfoDTO> values = fieldValueRepository.findAllEnabledValuesByFieldId(f.id());
                    MPNFieldsDTO fieldWithValues = new MPNFieldsDTO(f.id(), f.name(), f.dataType(), values);
                    return new MPNFieldsValuesDTO(fieldWithValues);
                }).toList();
    }

    public MPNInfoDTO update(MPNRequestDTO data, Long id) {
        var mpn = mpnRepository.findById(id).orElseThrow(() -> new RuntimeException("MPN not found"));
        var model = modelRepository.findById(data.modelId()).orElseThrow(() -> new RuntimeException("Model not found"));

        mpn.setName(data.name());
        mpn.setDescription(data.description());
        mpn.setModel(model);

        // Fetch existing MPN field values
        var existingFieldValues = mpnFieldValueRepository.findAllByMpnId(id);

        // Create a map of the existing field values for quick lookup
        Map<Long, Long> existingFieldValueMap = existingFieldValues.stream()
                .collect(Collectors.toMap(
                        fv -> fv.getFieldValue().getField().getId(),
                        fv -> fv.getFieldValue().getValueData().getId()
                ));

        // Create a map of the new field values for quick lookup
        Map<Long, Long> newFieldValueMap = data.mpnFieldsValues().stream()
                .collect(Collectors.toMap(MPNFieldValueRequestDTO::fieldId, MPNFieldValueRequestDTO::valueDataId));

        // Identify and delete the field values that are not in the request
        for (MPNFieldsValues existingFieldValue : existingFieldValues) {
            Long existingFieldId = existingFieldValue.getFieldValue().getField().getId();
            Long existingValueDataId = existingFieldValue.getFieldValue().getValueData().getId();
            if (!newFieldValueMap.containsKey(existingFieldId) || !newFieldValueMap.get(existingFieldId).equals(existingValueDataId)) {
                mpnFieldValueRepository.delete(existingFieldValue);
            }
        }

        // Identify and add new field values that are in the request but not in the existing values
        for (MPNFieldValueRequestDTO mfv : data.mpnFieldsValues()) {
            if (!existingFieldValueMap.containsKey(mfv.fieldId()) || !existingFieldValueMap.get(mfv.fieldId()).equals(mfv.valueDataId())) {
                var fieldValue = fieldValueRepository.findByFieldIdAndValueDataId(mfv.fieldId(), mfv.valueDataId());

                MPNFieldsValues newMpnFieldValue = new MPNFieldsValues();
                newMpnFieldValue.setMpn(mpn);
                newMpnFieldValue.setFieldValue(fieldValue);

                mpnFieldValueRepository.save(newMpnFieldValue);
            }
        }

//        // Collect the updated MPN field values for the response
//        List<MPNFieldValueInfoDTO> mpnFieldsValues = mpnFieldValueRepository.findAllByMpnId(id).stream()
//                .map(MPNFieldValueInfoDTO::new)
//                .toList();

        mpnRepository.save(mpn);

        return new MPNInfoDTO(mpn);
    }

    public MPNInfoDetailsDTO getMpnDetails(Long id) {
        // Fetch category details
        var model = mpnRepository.findById(id)
                .orElseThrow(() -> new ValidationException("MPN not found"));

        // Fetch fields and sort by fieldId
        List<MPNFieldValueInfoDTO> fields = mpnFieldValueRepository.findFieldsValuesByMpnId(id).stream()
                .sorted(Comparator.comparingLong(MPNFieldValueInfoDTO::fieldId))  // Sort by fieldId
                .collect(Collectors.toList());

        // Assemble the final DTO
        return new MPNInfoDetailsDTO(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getStatus(),
                model.getEnabled(),
                model.getModel().getId(),
                fields
        );
    }

    public List<MPNsByModelDTO> getMpnByModelId(Long modelId) {
        return mpnRepository.findAllByModelId(modelId);
    }
}
