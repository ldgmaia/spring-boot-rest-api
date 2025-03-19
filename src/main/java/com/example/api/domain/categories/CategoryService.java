package com.example.api.domain.categories;

import com.example.api.domain.ValidationException;
import com.example.api.domain.categories.validations.CategoryValidator;
import com.example.api.domain.categorycomponents.CategoryComponents;
import com.example.api.domain.categorycomponents.CategoryComponentsRegisterDTO;
import com.example.api.domain.categoryfields.*;
import com.example.api.domain.categorygroups.CategoryGroupsInfoDTO;
import com.example.api.domain.fields.Field;
import com.example.api.domain.values.ValueInfoDTO;
import com.example.api.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @Autowired
    private CategoryFieldRepository categoryFieldRepository;

    @Autowired
    private CategoryComponentRepository categoryComponentRepository;

    @Autowired
    private FieldValueRepository fieldValueRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private List<CategoryValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    public CategoryInfoDTO register(CategoryRequestDTO data) {

        validators.forEach(v -> v.validate(data));

        // Handling category
        var categoryGroup = data.categoryGroupId() != null ? categoryGroupRepository.getReferenceById(data.categoryGroupId()) : null;
        var category = new Category(new CategoryRegisterDTO(data.name(), categoryGroup, data.needsPost(), data.needsSerialNumber()));
        categoryRepository.save(category);

        // Handling fields
        List<CategoryFieldsUpdateDTO> fieldList = data.categoryFieldsValues();
        if (fieldList != null) {
            for (CategoryFieldsUpdateDTO field : fieldList) {

                var fieldExists = fieldRepository.findById(field.fieldId())
                        .orElseThrow(() -> new ValidationException("Field not found"));

                var categoryField = new CategoryFields(new CategoryFieldsRegisterDTO(field.dataLevel(), category, fieldExists, field.printOnLabel(), field.isMandatory()));
                categoryFieldRepository.save(categoryField);
            }
        }

        // Handling parent category
        List<Long> parentCategoryList = data.parentCategory();
        if (parentCategoryList != null) {
            for (Long categoryId : parentCategoryList) {

                var parentCategory = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ValidationException("Parent Category not found"));

                var categoryComponent = new CategoryComponents(new CategoryComponentsRegisterDTO(category, parentCategory));
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
        List<CategoryComponents> currentParentCategories = categoryComponentRepository.findByChildCategoryIdAndChildCategoryEnabledTrue(id);

        if (newParentCategoryIds == null || newParentCategoryIds.isEmpty()) {
            // If no parent categories are provided, delete all current parent categories
            categoryComponentRepository.deleteAll(currentParentCategories);
        } else {
            // Create a set of new parent category IDs for easier lookup
            Set<Long> newParentCategoryIdsSet = new HashSet<>(newParentCategoryIds);

            // Iterate through current parent categories and remove those not in the new list
            for (CategoryComponents currentParentCategory : currentParentCategories) {
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
                var categoryComponent = new CategoryComponents(new CategoryComponentsRegisterDTO(category, newParentCategory));
                categoryComponentRepository.save(categoryComponent);
            }
        }

        // handling category fields
        List<CategoryFieldsUpdateDTO> newFieldList = data.categoryFieldsValues();
        List<CategoryFields> currentFieldList = categoryFieldRepository.findAllByEnabledTrueAndCategoryId(id);

        // Create a map of current fields by field ID for easy lookup
        Map<Long, CategoryFields> currentFieldMap = currentFieldList.stream()
                .collect(Collectors.toMap(field -> field.getField().getId(), field -> field));

        // Process new field list
        for (CategoryFieldsUpdateDTO newFieldDTO : newFieldList) {
            Field field = fieldRepository.findById(newFieldDTO.fieldId())
                    .orElseThrow(() -> new ValidationException("Field not found"));

            CategoryFields currentField = currentFieldMap.get(newFieldDTO.fieldId());
            if (currentField != null) {
                // Update existing field if the fields_id matches
                currentField.setDataLevel(newFieldDTO.dataLevel());
                currentField.setPrintOnLabel(newFieldDTO.printOnLabel());
                currentField.setIsMandatory(newFieldDTO.isMandatory());
                currentField.setEnabled(true);
                categoryFieldRepository.save(currentField);
                currentFieldMap.remove(newFieldDTO.fieldId());
            } else {
                // Add new field
                CategoryFields newCategoryField = new CategoryFields();
                newCategoryField.setDataLevel(newFieldDTO.dataLevel());
                newCategoryField.setCategory(category);
                newCategoryField.setField(field);
                newCategoryField.setIsMandatory(newFieldDTO.isMandatory());
                newCategoryField.setPrintOnLabel(newFieldDTO.printOnLabel());
                newCategoryField.setEnabled(true);
                categoryFieldRepository.save(newCategoryField);
            }
        }

        // Disable remaining fields that were not updated or added
        for (CategoryFields remainingField : currentFieldMap.values()) {
            remainingField.setEnabled(false);
            categoryFieldRepository.save(remainingField);
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
        var parentCategory = categoryComponentRepository.findByChildCategoryIdAndChildCategoryEnabledTrue(id).stream()
                .map(component -> new CategoryInfoDTO(categoryRepository.getReferenceById(component.getParentCategory().getId())))
                .collect(Collectors.toList());
        //        Category parentCategory = categoryComponent != null ? categoryComponent.getParentCategory() : null;

        // NOTES: below are 2 ways of achieving the same outcome
        // NOTES: Here I am getting just the category ID of the component, and then create a list of instances using the DTO that I need
        // Fetch components
        var components = categoryComponentRepository.findByParentCategoryIdAndChildCategoryEnabledTrue(id).stream()
                .map(component -> new CategoryInfoDTO(categoryRepository.getReferenceById(component.getChildCategory().getId())))
                .collect(Collectors.toList());

        // NOTES: And in this example I defined the in the repository the format according to my DTO.
        //        I prefer the first approach compared to this one, since is clear to understand what is happening, but this second way is less verbose in the service class
        // Fetch category fields
//        var categoryFields = categoryFieldsRepository.findAllEnabledByCategoryId(id);
        var categoryFields = categoryFieldRepository.findAllEnabledByCategoryId(id).stream()
                .map(categoryField -> {
                    List<ValueInfoDTO> values = fieldValueRepository.findAllEnabledValuesByFieldId(categoryField.fieldId());
                    return new CategoryFieldsValuesInfoDTO(categoryFieldRepository.getReferenceById(categoryField.id()), values);
                })
                .sorted(Comparator.comparingLong(CategoryFieldsValuesInfoDTO::fieldId))  // Sort by fieldId
                .toList();

        // Assemble the final DTO
        return new CategoryInfoDetailsDTO(
                category.getId(),
                category.getName(),
                category.getNeedsPost(),
                category.getNeedsSerialNumber(),
                new CategoryGroupsInfoDTO(categoryGroup),
                parentCategory,
                components,
                categoryFields
        );
    }

    public List<CategoryFieldsAssessmentInfoDTO> getAssessmentMainItemFieldsByinventoryItemId(Long inventoryItemId) {
        var categoryId = inventoryItemRepository.findById(inventoryItemId).orElseThrow().getCategory().getId();
        return categoryRepository.findById(categoryId)
                .map(category -> category.getCategoryFields().stream()
                        .filter(CategoryFields::getEnabled)
                        .map(CategoryFieldsAssessmentInfoDTO::new)
                        .toList())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
    }

    public List<CategoryFieldsMainItemInspectionInfoDTO> getInspectionMainItemFieldsByinventoryItemId(Long inventoryItemId) {
        var categoryId = inventoryItemRepository.findById(inventoryItemId).orElseThrow().getCategory().getId();
        return categoryRepository.findById(categoryId)
                .map(category -> category.getCategoryFields().stream()
                        .filter(CategoryFields::getEnabled)
                        .map(cf -> new CategoryFieldsMainItemInspectionInfoDTO(cf, inventoryItemRepository, inventoryItemId))
                        .toList())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
    }
}
