package com.example.api.domain.mpns;

import com.example.api.domain.ValidationException;
import com.example.api.domain.mpnfieldsvalues.MPNFieldValueInfoDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldValueRegisterDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldValueRequestDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldsValues;
import com.example.api.domain.values.ValueInfoDTO;
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
    private MPNFieldValueRepository mpnFieldValueRepository;

    @Autowired
    private MPNRepository mpnRepository;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private List<FieldValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public MPNInfoDTO register(MPNRequestDTO data) {

        // Fetch the currently logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByUsername(username);

        var mpn = new MPN(new MPNRegisterDTO(data.name(), data.description(), data.status(), modelRepository.getReferenceById(data.modelId())), currentUser);
        mpnRepository.save(mpn);


        if (data.mpnFieldsValues() != null) {
            data.mpnFieldsValues().forEach(mfv -> {
                var fieldValue = fieldValueRepository.findByFieldIdAndValueDataId(mfv.fieldId(), mfv.valueDataId());

                var mpnFieldValue = new MPNFieldsValues(new MPNFieldValueRegisterDTO(fieldValue, mpn));
                mpnFieldValueRepository.save(mpnFieldValue);
            });
        }
//        return new MPNInfoDTO(mpn, mpnFieldsValues != null ? mpnFieldsValues : List.of());
        return new MPNInfoDTO(mpn);
    }

    public List<MPNFieldsValuesDTO> listMpnFields(Long modelId) {
        var fieldList = mpnRepository.findMPNFieldsByModelId(modelId);

        List<MPNFieldsValuesDTO> mpnFieldsValues = fieldList.stream()
                .map(f -> {
                    List<ValueInfoDTO> values = fieldValueRepository.findAllEnabledValuesByFieldId(f.id());
                    MPNFieldsDTO fieldWithValues = new MPNFieldsDTO(f.id(), f.name(), values);
                    return new MPNFieldsValuesDTO(fieldWithValues);
                }).toList();

        return mpnFieldsValues;
    }

//    public MPNInfoDTO get(Long id) {
//        var mpn = mpnRepository.getReferenceById(id);
//
//        // Collect the updated MPN field values for the response
//        List<MPNFieldValueInfoDTO> mpnFieldsValues = mpnFieldValueRepository.findAllByMpnId(id).stream()
//                .map(MPNFieldValueInfoDTO::new)
//                .toList();
//
//        return new MPNInfoDTO(mpn, mpnFieldsValues);
//    }


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
}
