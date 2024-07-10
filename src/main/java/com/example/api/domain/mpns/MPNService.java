package com.example.api.domain.mpns;

import com.example.api.domain.mpnfieldsvalues.MPNFieldValueInfoDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldValueRegisterDTO;
import com.example.api.domain.mpnfieldsvalues.MPNFieldsValues;
import com.example.api.domain.values.ValueInfoDTO;
import com.example.api.repositories.FieldValueRepository;
import com.example.api.repositories.MPNFieldValueRepository;
import com.example.api.repositories.MPNRepository;
import com.example.api.repositories.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MPNService {

//    @Autowired
//    private CategoryRepository categoryRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private FieldValueRepository fieldValueRepository;

//    @Autowired
//    private ModelFieldValueRepository modelFieldValueRepository;
//
//    @Autowired
//    private SectionRepository sectionRepository;

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

//        List<MPNFieldsValuesDTO> mpnFieldsValues = fieldList.stream().map(f -> {
//            List<ValueInfoDTO> values = fieldValueRepository.findAllEnabledValuesByFieldId(f.id());
//            return new MPNFieldsValuesDTO(f, values);
//        }).toList();

        List<MPNFieldsValuesDTO> mpnFieldsValues = fieldList.stream().map(f -> {
            List<ValueInfoDTO> values = fieldValueRepository.findAllEnabledValuesByFieldId(f.id());
            MPNFieldsDTO fieldWithValues = new MPNFieldsDTO(f.id(), f.name(), values);
            return new MPNFieldsValuesDTO(fieldWithValues);
        }).toList();

        return mpnFieldsValues;
    }
}
