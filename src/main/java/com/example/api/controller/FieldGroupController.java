package com.example.api.controller;

import com.example.api.domain.fieldgroups.*;
import com.example.api.repositories.FieldGroupRepository;
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
@RequestMapping("field-groups")
@SecurityRequirement(name = "bearer-key")
public class FieldGroupController {

    @Autowired
    private FieldGroupRepository fieldGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid FieldGroupRegisterDTO data, UriComponentsBuilder uriBuilder) {

        var fieldGroupExists = fieldGroupRepository.existsByName(data.name());

        if (fieldGroupExists) {
//            return ResponseEntity.status(409).header("X-Custom-Message", "Filed group " + data.name() + " is already registered").build();
//            return ResponseEntity.status(HttpStatus.CONFLICT).header(HttpHeaders.LOCATION, "http://resource/id").build();
//            return ResponseEntity.status(HttpStatus.CONFLICT).header(HttpHeaders.LOCATION, "http://resource/id").body("Filed group " + data.name() + " is already registered");
            Map<String, String> jsonResponse = Map.of("message", "Field group " + data.name() + " is already registered");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResponse);
        }

        var fieldGroup = new FieldGroup(data);

        fieldGroupRepository.save(fieldGroup);

        var uri = uriBuilder.path("/field-groups/{id}").buildAndExpand(fieldGroup.getId()).toUri();

        return ResponseEntity.created(uri).body(new FieldGroupInfoDTO(fieldGroup));
    }

    @GetMapping
    public ResponseEntity<Page<FieldGroupListDTO>> list(HttpServletRequest request, @PageableDefault(size = 10, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {

        var page = fieldGroupRepository.findAllByEnabledTrue(pagination).map(FieldGroupListDTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid FieldGroupUpdateDTO data) {

        var fieldGroupExists = fieldGroupRepository.existsById(data.id());

        if (!fieldGroupExists) {
            Map<String, String> jsonResponse = Map.of("message", "Field group " + data.name() + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
        }

        var fieldGroup = fieldGroupRepository.getReferenceById(data.id());
        fieldGroup.updateInfo(data);

        return ResponseEntity.ok(new FieldGroupInfoDTO(fieldGroup));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) { // route is /doctors/1, for example
        //        repository.deleteById(id); // hard delete from database
        var fieldGroup = fieldGroupRepository.getReferenceById(id);

        if (!fieldGroup.getEnabled()) {
            return ResponseEntity.status(304).header("X-Custom-Message", "Field group is already disabled").build();
        }

        fieldGroup.deactivate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        try {
            var fieldGroup = fieldGroupRepository.getReferenceById(id);
            return ResponseEntity.ok(new FieldGroupInfoDTO(fieldGroup));
        } catch (EntityNotFoundException ex) {
            Map<String, String> jsonResponse = Map.of("message", "Field Group not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
        }
    }
}
