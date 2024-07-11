package com.example.api.domain.mpns;

import com.example.api.domain.mpnfieldsvalues.MPNFieldValueInfoDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldValueRegisterDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldValueRequestDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldsValues;
import com.example.api.domain.values.ValueInfoDTO;
import com.example.api.repositories.FieldValueRepository;
import com.example.api.repositories.MPNFieldValueRepository;
import com.example.api.repositories.MPNRepository;
import com.example.api.repositories.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private MPNFieldValueRepository mpnFieldValueRepository;

    @Autowired
    private MPNRepository mpnRepository;

//    @Autowired
//    private List<FieldValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically


    public MPNInfoDTO register(MPNRequestDTO data) {

        var mpn = new MPN(new MPNRegisterDTO(data.name(), data.description(), data.status(), modelRepository.getReferenceById(data.modelId())));
        mpnRepository.save(mpn);

        if (data.mpnFieldsValues() != null) {
            List<MPNFieldValueInfoDTO> mpnFieldsValues = data.mpnFieldsValues().stream().map(mfv -> {
                var fieldValue = fieldValueRepository.findByFieldIdAndValueDataId(mfv.fieldId(), mfv.valueDataId());

                var mpnFieldValue = new MPNFieldsValues(new MPNFieldValueRegisterDTO(fieldValue, mpn));
                mpnFieldValueRepository.save(mpnFieldValue);
                return new MPNFieldValueInfoDTO(mpnFieldValue);
            }).toList();
        }
        return new MPNInfoDTO(mpn);
    }

    public List<MPNFieldsValuesDTO> listMpnFields(Long modelId) {
        var fieldList = mpnRepository.findMPNFieldsByModelId(modelId);

        List<MPNFieldsValuesDTO> mpnFieldsValues = fieldList.stream().map(f -> {
            List<ValueInfoDTO> values = fieldValueRepository.findAllEnabledValuesByFieldId(f.id());
            MPNFieldsDTO fieldWithValues = new MPNFieldsDTO(f.id(), f.name(), values);
            return new MPNFieldsValuesDTO(fieldWithValues);
        }).toList();

        return mpnFieldsValues;
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

        mpnRepository.save(mpn);

        return new MPNInfoDTO(mpn);
    }
}
