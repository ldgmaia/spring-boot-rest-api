package com.example.api.controller;

import com.example.api.domain.fields.*;
import com.example.api.repositories.FieldRepository;
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
@RequestMapping("fields")
@SecurityRequirement(name = "bearer-key")
public class FieldsController {

    @Autowired
    private FieldService fieldService;

    @Autowired
    private FieldRepository fieldRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid FieldRequestDTO data, UriComponentsBuilder uriBuilder) {

        var field = fieldService.register(data);
        var uri = uriBuilder.path("/fields/{id}").buildAndExpand(field.id()).toUri();

        return ResponseEntity.created(uri).body(field);
    }

    @GetMapping
    public ResponseEntity<Page<FieldListDTO>> list(HttpServletRequest request, @PageableDefault(size = 10, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = fieldRepository.findAllByEnabledTrue(pagination).map(FieldListDTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid FieldUpdateDTO data) {
//        var field = fieldRepository.getReferenceById(data.id());
        var field = fieldService.updateInfo(data);

        return ResponseEntity.ok(field);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) { // route is /doctors/1, for example
//        repository.deleteById(id); // hard delete from database
        var field = fieldRepository.getReferenceById(id);

        if (!field.getEnabled()) {
            return ResponseEntity.status(304).header("X-Custom-Message", "Field is already disabled").build();
        }

        field.deactivate();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        var field = fieldRepository.getReferenceById(id);
        return ResponseEntity.ok(new FieldInfoDTO(field));
    }

    @GetMapping("/field-group/{fieldGroupId}")
    public ResponseEntity<FieldsByGroupDTO> getEnabledFieldsByFieldGroupId(
            @PathVariable Long fieldGroupId
    ) {
        FieldsByGroupDTO response = fieldService.getEnabledFieldsByFieldGroupId(fieldGroupId);
        return ResponseEntity.ok(response);
    }

//    public ResponseEntity<Page<FieldListDTO>> getFieldsByGroup(
//            @PathVariable Long fieldGroupId,
//            Pageable pageable
//    ) {
//        Page<Field> fieldsPage = fieldService.getEnabledFieldsByFieldGroupId(fieldGroupId, pageable);
//        Page<FieldListDTO> fieldsListDTOPage = fieldsPage.map(FieldListDTO::new);
//        return ResponseEntity.ok(fieldsListDTOPage);
//    }
}
