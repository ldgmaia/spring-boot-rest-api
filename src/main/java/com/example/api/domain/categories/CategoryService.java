package com.example.api.domain.categories;

import com.example.api.domain.ValidationException;
import com.example.api.domain.categories.validations.CategoryValidator;
import com.example.api.domain.categorycomponent.CategoryComponent;
import com.example.api.domain.categorycomponent.CategoryComponentRegisterDTO;
import com.example.api.domain.categoryfield.CategoryField;
import com.example.api.domain.categoryfield.CategoryFieldRegisterDTO;
import com.example.api.domain.categoryfield.CategoryFieldRequestDTO;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<CategoryFieldRequestDTO> fieldList = data.fields();
        if (fieldList != null) {
            for (CategoryFieldRequestDTO field : fieldList) {

                var fieldExists = fieldRepository.findById(field.fieldId())
                        .orElseThrow(() -> new ValidationException("Field not found"));

                System.out.println("fieldExists " + fieldExists);
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
            var parentCategoryExists = categoryRepository.existsById(data.categoryGroupId());
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
        List<CategoryFieldRequestDTO> newfieldList = data.fields();

        List<CategoryFieldRequestDTO> currentFieldList = categoryFieldsRepository.findAllByEnabledTrueAndCategoryId(id);

        return new CategoryInfoDTO(category);
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
