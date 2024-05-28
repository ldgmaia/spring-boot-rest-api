package com.example.api.domain.categories;

import com.example.api.domain.ValidationException;
import com.example.api.domain.categories.validations.CategoryValidator;
import com.example.api.domain.categorycomponent.CategoryComponent;
import com.example.api.domain.categorycomponent.CategoryComponentRegisterDTO;
import com.example.api.domain.categoryfield.CategoryField;
import com.example.api.domain.categoryfield.CategoryFieldRegisterDTO;
import com.example.api.domain.categoryfield.CategoryFieldUpdateDTO;
import com.example.api.domain.fields.Field;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @Autowired
    private CategoryFieldsRepository categoryFieldsRepository;

    @Autowired
    private CategoryComponentRepository categoryComponentRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private List<CategoryValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public CategoryInfoDTO register(CategoryRequestDTO data) {

        validators.forEach(v -> v.validate(data));

        // Handling category
        var categoryGroup = data.categoryGroupId() != null ? categoryGroupRepository.getReferenceById(data.categoryGroupId()) : null;
        var category = new Category(new CategoryRegisterDTO(data.name(), categoryGroup, data.needsPost(), data.needsSerialNumber()));
        categoryRepository.save(category);

        // Handling fields
        List<CategoryFieldUpdateDTO> fieldList = data.fields();
        if (fieldList != null) {
            for (CategoryFieldUpdateDTO field : fieldList) {

                var fieldExists = fieldRepository.findById(field.fieldId())
                        .orElseThrow(() -> new ValidationException("Field not found"));

                var categoryField = new CategoryField(new CategoryFieldRegisterDTO(field.dataLevel(), category, fieldExists, field.printOnLabel(), field.isMandatory()));
                categoryFieldsRepository.save(categoryField);
            }
        }

        // Handling parent category
        if (data.parentCategoryId() != null) {
            var parentCategory = categoryRepository.findById(data.parentCategoryId())
                    .orElseThrow(() -> new ValidationException("Parent Category not found"));
            var categoryComponent = new CategoryComponent(new CategoryComponentRegisterDTO(category, parentCategory));
            categoryComponentRepository.save(categoryComponent);
        }

        return new CategoryInfoDTO(category);
    }

    public CategoryInfoDTO update(CategoryRequestDTO data, Long id) {

        // handling category details
        var category = categoryRepository.getReferenceById(id);
        category.setName(data.name());
        category.setNeedsPost(data.needsPost());
        category.setNeedsSerialNumber(data.needsSerialNumber());

        // handling Category Group
        var categoryGroupExists = categoryGroupRepository.existsById(data.categoryGroupId());
        if (categoryGroupExists) {
            category.setCategoryGroup(categoryGroupRepository.getReferenceById(data.categoryGroupId()));
        }

        // handling parent category
        var parentCategoryId = data.parentCategoryId();
        if (parentCategoryId != null) {
            var parentCategoryExists = categoryRepository.existsById(parentCategoryId);
            if (parentCategoryExists) {
                var parentCategory = categoryComponentRepository.findCategoryComponentByChildCategoryId(category.getId());
                var newParentCategory = categoryRepository.findById(data.parentCategoryId())
                        .orElseThrow(() -> new ValidationException("Parent Category not found"));

                if (parentCategory != null) {
                    parentCategory.setParentCategory(newParentCategory);
                } else {
                    var categoryComponent = new CategoryComponent(new CategoryComponentRegisterDTO(category, newParentCategory));
                    categoryComponentRepository.save(categoryComponent);
                }
            }
        } else {
            var parentCategory = categoryComponentRepository.findCategoryComponentByChildCategoryId(category.getId());
            if (parentCategory != null) {
                categoryComponentRepository.deleteById(parentCategory.getId()); // hard delete from database
            }

        }

        // handling category fields
        List<CategoryFieldUpdateDTO> newFieldList = data.fields();
        List<CategoryField> currentFieldList = categoryFieldsRepository.findAllByEnabledTrueAndCategoryId(id);

        // Create a map of current fields by field ID for easy lookup
        Map<Long, CategoryField> currentFieldMap = currentFieldList.stream()
                .collect(Collectors.toMap(field -> field.getField().getId(), field -> field));

        // Process new field list
        for (CategoryFieldUpdateDTO newFieldDTO : newFieldList) {
            Field field = fieldRepository.findById(newFieldDTO.fieldId())
                    .orElseThrow(() -> new ValidationException("Field not found"));

            CategoryField currentField = currentFieldMap.get(newFieldDTO.fieldId());
            if (currentField != null) {
                // Update existing field if the fields_id matches
                currentField.setDataLevel(newFieldDTO.dataLevel());
                currentField.setPrintOnLabel(newFieldDTO.printOnLabel());
                currentField.setIsMandatory(newFieldDTO.isMandatory());
                currentField.setEnabled(true);
                categoryFieldsRepository.save(currentField);
                currentFieldMap.remove(newFieldDTO.fieldId());
            } else {
                // Add new field
                CategoryField newCategoryField = new CategoryField();
                newCategoryField.setDataLevel(newFieldDTO.dataLevel());
                newCategoryField.setCategory(category);
                newCategoryField.setField(field);
                newCategoryField.setIsMandatory(newFieldDTO.isMandatory());
                newCategoryField.setPrintOnLabel(newFieldDTO.printOnLabel());
                newCategoryField.setEnabled(true);
                categoryFieldsRepository.save(newCategoryField);
            }
        }

        // Disable remaining fields that were not updated or added
        for (CategoryField remainingField : currentFieldMap.values()) {
            remainingField.setEnabled(false);
            categoryFieldsRepository.save(remainingField);
        }

        return new CategoryInfoDTO(category);
    }

    public CategoryInfoDetailsDTO getCategoryDetails(Long id) {
        // Fetch category details
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Category not found"));

        // Fetch parent category details
        var categoryComponent = categoryComponentRepository.findCategoryComponentByChildCategoryId(id);
        Category parentCategory = categoryComponent != null ? categoryComponent.getParentCategory() : null;

        // Fetch components

//        var componentList = categoryComponentRepository.findComponentsByParentCategoryId(id);
//        System.out.println("componentList " + componentList);

        var components = categoryComponentRepository.findComponentsByParentCategoryId(id).stream()
                .map(component -> new CategoryInfoDTO(categoryRepository.getReferenceById(component.getChildCategory().getId())))
                .collect(Collectors.toList());

        // Fetch components
//        List<CategoryComponentInfoDTO> components = categoryComponentRepository.findByParentCategoryId(id).stream()
//                .map(component -> new CategoryComponentInfoDTO(
//                        component.id(),
//                        component.name(),
//                        component.needsPost(),
//                        component.needsSerialNumber()
//                ))
//                .collect(Collectors.toList());

        // Fetch category fields
//        List<CategoryFieldsInfoDTO> categoryFields = categoryFieldsRepository.findAllByEnabledTrueAndCategoryId(id).stream()
//                .map(field -> {
//                    Field fieldDetails = field.getField();
//                    return new CategoryFieldsInfoDTO(
//                            field.getId(),
//                            field.getDataLevel().name(),
//                            field.getPrintOnLabel(),
//                            field.getIsMandatory(),
//                            fieldDetails.getId(),
//                            fieldDetails.getName(),
//                            fieldDetails.getDataType(),
//                            fieldDetails.getFieldType(),
//                            fieldDetails.getIsMultiple()
//                    );
//                })
//                .collect(Collectors.toList());

        // Assemble the final DTO
        return new CategoryInfoDetailsDTO(
                category.getId(),
                category.getName(),
                category.getNeedsPost(),
                category.getNeedsSerialNumber(),
                parentCategory != null ? new CategoryInfoDTO(parentCategory) : null,
                components
//                new CategoryGroupInfoDTO(category.getCategoryGroup()),


//                categoryFields
        );
    }

////    public Page<Field> getAllEnabledFieldsByFieldGroupId(Long fieldGroupId, Pageable pageable) {
////        return fieldRepository.findByEnabledTrueAndFieldGroup_Id(fieldGroupId, pageable);
////    }
//
//    public CategoryByGroupDTO getEnabledFieldsByFieldGroupId(Long fieldGroupId) {
//        var fieldGroup = fieldGroupRepository.findById(fieldGroupId).orElse(null);
//        if (fieldGroup == null) {
//            throw new ValidationException("Field Group ID not found");
//        }
//
//        List<Category> fields = fieldRepository.findByEnabledTrueAndFieldGroup_Id(fieldGroupId);
//        List<FieldListDTO> fieldsListDTO = fields.stream().map(FieldListDTO::new).collect(Collectors.toList());
//
//        return new CategoryByGroupDTO(fieldGroup.getName(), fieldsListDTO);
//    }
}
