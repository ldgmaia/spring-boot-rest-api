package com.example.api.controller;

import com.example.api.domain.categorygroups.*;
import com.example.api.repositories.CategoryGroupRepository;
import com.example.api.repositories.CategoryRepository;
import com.example.api.repositories.UserPermissionRepository;
import com.example.api.repositories.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("category-groups")
@SecurityRequirement(name = "bearer-key")
public class CategoryGroupController {

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid CategoryGroupRegisterDTO data, UriComponentsBuilder uriBuilder) {

        var categoryGroupExists = categoryGroupRepository.existsByName(data.name());

        if (categoryGroupExists) {
            Map<String, String> jsonResponse = Map.of("message", "Category group " + data.name() + " is already registered");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResponse);
        }

        var categoryGroup = new CategoryGroup(data);

        categoryGroupRepository.save(categoryGroup);

        var uri = uriBuilder.path("/category-groups/{id}").buildAndExpand(categoryGroup.getId()).toUri();

        return ResponseEntity.created(uri).body(new CategoryGroupInfoDTO(categoryGroup));
    }

    @GetMapping
    public ResponseEntity<Page<CategoryGroupInfoDTO>> list(HttpServletRequest request, @PageableDefault(size = 10, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {

        var page = categoryGroupRepository.findAllByEnabledTrue(pagination).map(CategoryGroupInfoDTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity update(@RequestBody @Valid CategoryGroupUpdateDTO data, @PathVariable Long id) {

        var categoryGroupExists = categoryGroupRepository.existsById(id);

        if (!categoryGroupExists) {
            Map<String, String> jsonResponse = Map.of("message", "Category group " + data.name() + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
        }

        var categoryGroup = categoryGroupRepository.getReferenceById(id);
        categoryGroup.updateInfo(data);

        return ResponseEntity.ok(new CategoryGroupInfoDTO(categoryGroup));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) { // route is /doctors/1, for example

        var categoryGroupHasCategories = categoryRepository.existsByCategoryGroupId(id);
        if (categoryGroupHasCategories) {
            Map<String, String> jsonResponse = Map.of("message", "Category group has categories under it and cannot be deactivated");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResponse);
        }

        //        repository.deleteById(id); // hard delete from database
        var categoryGroup = categoryGroupRepository.getReferenceById(id);

        if (!categoryGroup.getEnabled()) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).header("X-Custom-Message", "Category group is already disabled").build();
        }

        categoryGroup.deactivate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        try {
            var categoryGroup = categoryGroupRepository.getReferenceById(id);

            var categories = categoryRepository.findAllByCategoryGroupId(id);

            return ResponseEntity.ok(new CategoryGroupInfoDetailsDTO(categoryGroup.getId(), categoryGroup.getName(), categoryGroup.getEnabled(), categories));
        } catch (EntityNotFoundException ex) {
            Map<String, String> jsonResponse = Map.of("message", "Category group not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
        }
    }
}
