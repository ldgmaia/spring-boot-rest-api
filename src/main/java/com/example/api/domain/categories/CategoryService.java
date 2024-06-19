package com.example.api.domain.categories;

import com.example.api.domain.ValidationException;
import com.example.api.domain.categories.validations.CategoryValidator;
import com.example.api.domain.categorycomponent.CategoryComponent;
import com.example.api.domain.categorycomponent.CategoryComponentRegisterDTO;
import com.example.api.domain.categoryfield.CategoryField;
import com.example.api.domain.categoryfield.CategoryFieldRegisterDTO;
import com.example.api.domain.categoryfield.CategoryFieldUpdateDTO;
import com.example.api.domain.categoryfield.CategoryFieldsValuesInfoDTO;
import com.example.api.domain.categorygroups.CategoryGroupInfoDTO;
import com.example.api.domain.fields.Field;
import com.example.api.domain.values.ValueInfoDTO;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private FieldValueRepository fieldValueRepository;

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
        List<Long> parentCategoryList = data.parentCategory();
        if (parentCategoryList != null) {
            for (Long categoryId : parentCategoryList) {

                var parentCategory = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ValidationException("Parent Category not found"));

                var categoryComponent = new CategoryComponent(new CategoryComponentRegisterDTO(category, parentCategory));
                categoryComponentRepository.save(categoryComponent);
            }
        }

        // below is my old code when I was receiving only one category parent ID
//        if (data.parentCategoryId() != null) {
//            var parentCategory = categoryRepository.findById(data.parentCategoryId())
//                    .orElseThrow(() -> new ValidationException("Parent Category not found"));
//            var categoryComponent = new CategoryComponent(new CategoryComponentRegisterDTO(category, parentCategory));
//            categoryComponentRepository.save(categoryComponent);
//        }

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

        // Update the method below to achieve what was asked
        // handling parent category
        // handling parent categories
        List<Long> newParentCategoryIds = data.parentCategory();
        List<CategoryComponent> currentParentCategories = categoryComponentRepository.findByChildCategoryId(id);

        if (newParentCategoryIds == null || newParentCategoryIds.isEmpty()) {
            // If no parent categories are provided, delete all current parent categories
            categoryComponentRepository.deleteAll(currentParentCategories);
        } else {
            // Create a set of new parent category IDs for easier lookup
            Set<Long> newParentCategoryIdsSet = new HashSet<>(newParentCategoryIds);

            // Iterate through current parent categories and remove those not in the new list
            for (CategoryComponent currentParentCategory : currentParentCategories) {
                if (!newParentCategoryIdsSet.contains(currentParentCategory.getParentCategory().getId())) {
                    categoryComponentRepository.delete(currentParentCategory);
                } else {
                    // Remove it from the set if it's already present
                    newParentCategoryIdsSet.remove(currentParentCategory.getParentCategory().getId());
                }
            }

            // Add the remaining new parent categories that were not in the current list
            for (Long newParentCategoryId : newParentCategoryIdsSet) {
                var newParentCategory = categoryRepository.findById(newParentCategoryId)
                        .orElseThrow(() -> new ValidationException("Parent Category not found"));
                var categoryComponent = new CategoryComponent(new CategoryComponentRegisterDTO(category, newParentCategory));
                categoryComponentRepository.save(categoryComponent);
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

        // Get Category Group info
        var categoryGroup = categoryGroupRepository.getReferenceById(category.getCategoryGroup().getId());

        // Fetch parent category details
        var parentCategory = categoryComponentRepository.findCategoryComponentByChildCategoryId(id).stream()
                .map(component -> new CategoryInfoDTO(categoryRepository.getReferenceById(component.getParentCategory().getId())))
                .collect(Collectors.toList());
        ;
//        Category parentCategory = categoryComponent != null ? categoryComponent.getParentCategory() : null;

        // NOTES: below are 2 ways of achieving the same outcome
        // NOTES: Here I am getting just the category ID of the component, and then create a list of instances using the DTO that I need
        // Fetch components
        var components = categoryComponentRepository.findComponentsByParentCategoryId(id).stream()
                .map(component -> new CategoryInfoDTO(categoryRepository.getReferenceById(component.getChildCategory().getId())))
                .collect(Collectors.toList());

        // NOTES: And in this example I defined the in the repository the format according to my DTO.
        //        I prefer the first approach compared to this one, since is clear to understand what is happening, but this second way is less verbose in the service class
        // Fetch category fields
//        var categoryFields = categoryFieldsRepository.findAllEnabledByCategoryId(id);
        var categoryFields = categoryFieldsRepository.findAllEnabledByCategoryId(id).stream()
                .map(categoryField -> {
                    List<ValueInfoDTO> values = fieldValueRepository.findAllEnabledValuesByFieldId(categoryField.fieldId());
                    return new CategoryFieldsValuesInfoDTO(categoryFieldsRepository.getReferenceById(categoryField.id()), values);
                })
                .toList();

        // Assemble the final DTO
        return new CategoryInfoDetailsDTO(
                category.getId(),
                category.getName(),
                category.getNeedsPost(),
                category.getNeedsSerialNumber(),
                new CategoryGroupInfoDTO(categoryGroup),
                parentCategory,
                components,
                categoryFields
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
