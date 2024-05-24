package com.example.api.controller;

import com.example.api.domain.categories.CategoryListDTO;
import com.example.api.domain.categories.CategoryRequestDTO;
import com.example.api.domain.categories.CategoryService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("categories")
@SecurityRequirement(name = "bearer-key")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid CategoryRequestDTO data, UriComponentsBuilder uriBuilder) {


        var category = categoryService.register(data);
        var uri = uriBuilder.path("/category/{id}").buildAndExpand(category.id()).toUri();


        return ResponseEntity.created(uri).body(category);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryListDTO>> list(HttpServletRequest request, @PageableDefault(size = 10, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = categoryRepository.findAllByEnabledTrue(pagination).map(CategoryListDTO::new);
        return ResponseEntity.ok(page);
    }

//    @PutMapping("/{id}")
//    @Transactional
//    public ResponseEntity update(@RequestBody @Valid CategoryRequestDTO data, @PathVariable Long id) {
////        var field = fieldRepository.getReferenceById(data.id());
//        var category = categoryService.update(data, id);
//
//        return ResponseEntity.ok(category);
//    }

//    @DeleteMapping("/{id}")
//    @Transactional
//    public ResponseEntity delete(@PathVariable Long id) { // route is /doctors/1, for example
////        repository.deleteById(id); // hard delete from database
//        var field = fieldRepository.getReferenceById(id);
//
//        if (!field.getEnabled()) {
//            return ResponseEntity.status(304).header("X-Custom-Message", "Field is already disabled").build();
//        }
//
//        field.deactivate();
//
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity detail(@PathVariable Long id) {
//        try {
//            var field = fieldRepository.getReferenceById(id);
//            return ResponseEntity.ok(new FieldInfoDTO(field));
//        } catch (EntityNotFoundException ex) {
//            Map<String, String> jsonResponse = Map.of("message", "Field not found");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
//        }
//    }
//
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
