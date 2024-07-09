package com.example.api.domain.mpns;

import com.example.api.domain.values.ValueInfoDTO;
import com.example.api.repositories.FieldValueRepository;
import com.example.api.repositories.MPNRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MPNService {

//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private ModelRepository modelRepository;
//
//    @Autowired
//    private FieldRepository fieldRepository;

    @Autowired
    private FieldValueRepository fieldValueRepository;

//    @Autowired
//    private ModelFieldValueRepository modelFieldValueRepository;
//
//    @Autowired
//    private SectionRepository sectionRepository;

//    @Autowired
//    private ValueRepository valueRepository;

    @Autowired
    private MPNRepository mpnRepository;


//    @Autowired
//    private List<FieldValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically


//    public MPNInfoDTO register(MPNRequestDTO data) {
//
//    }

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
