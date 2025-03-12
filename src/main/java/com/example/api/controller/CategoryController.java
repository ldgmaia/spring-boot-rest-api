package com.example.api.controller;

import com.example.api.domain.ValidationException;
import com.example.api.domain.categories.CategoryListDTO;
import com.example.api.domain.categories.CategoryRequestDTO;
import com.example.api.domain.categories.CategoryService;
import com.example.api.repositories.CategoryFieldRepository;
import com.example.api.repositories.CategoryRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("categories")
@SecurityRequirement(name = "bearer-key")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryFieldRepository categoryFieldRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid CategoryRequestDTO data, UriComponentsBuilder uriBuilder) {

        var category = categoryService.register(data);
        var uri = uriBuilder.path("/category/{id}").buildAndExpand(category.id()).toUri();

        return ResponseEntity.created(uri).body(category);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryListDTO>> list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = categoryRepository.findAllByEnabledTrue(pagination).map(CategoryListDTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity update(@RequestBody @Valid CategoryRequestDTO data, @PathVariable Long id) {
        var category = categoryService.update(data, id);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {

        var categoryHasFields = categoryFieldRepository.existsByCategoryId(id);
        if (categoryHasFields) {
            Map<String, String> jsonResponse = Map.of("message", "Category has fields and cannot be deactivated");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResponse);
        }

//        repository.deleteById(id); // hard delete from database
        var category = categoryRepository.getReferenceById(id);

        if (!category.getEnabled()) {
            Map<String, String> jsonResponse = Map.of("message", "Category " + category.getName() + " is already deactivated");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResponse);
        }

        category.deactivate();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Category not found"));

        var categoryDetails = categoryService.getCategoryDetails(id);
        return ResponseEntity.ok(categoryDetails);
    }

//    @GetMapping("/field-group/{fieldGroupId}")
//    public ResponseEntity<FieldsByGroupDTO> getEnabledFieldsByFieldGroupId(
//            @PathVariable Long fieldGroupId
//    ) {
//        FieldsByGroupDTO response = fieldService.getEnabledFieldsByFieldGroupId(fieldGroupId);
//        return ResponseEntity.ok(response);
//    }

//    public ResponseEntity<Page<FieldListDTO>> getFieldsByGroup(
//            @PathVariable Long fieldGroupId,
//            Pageable pageable
//    ) {
//        Page<Field> fieldsPage = fieldService.getEnabledFieldsByFieldGroupId(fieldGroupId, pageable);
//        Page<FieldListDTO> fieldsListDTOPage = fieldsPage.map(FieldListDTO::new);
//        return ResponseEntity.ok(fieldsListDTOPage);
//    }
}
