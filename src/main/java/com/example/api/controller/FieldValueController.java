package com.example.api.controller;

import com.example.api.domain.fieldsvalues.FieldValueRequestDTO;
import com.example.api.domain.fieldsvalues.FieldValueService;
import com.example.api.repositories.FieldRepository;
import com.example.api.repositories.FieldValueRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("field-values")
@SecurityRequirement(name = "bearer-key")
public class FieldValueController {

    @Autowired
    private FieldValueService fieldValueService;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FieldValueRepository fieldValueRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid FieldValueRequestDTO data, UriComponentsBuilder uriBuilder) {

        var fieldValue = fieldValueService.register(data);
        var uri = uriBuilder.path("/field-values/{id}").buildAndExpand(fieldValue.id()).toUri();

        return ResponseEntity.created(uri).body(fieldValue);
    }

//    @GetMapping
//    public ResponseEntity<Page<FieldListDTO>> list(HttpServletRequest request, @PageableDefault(size = 10, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
//        var page = fieldRepository.findAllByEnabledTrue(pagination).map(FieldListDTO::new);
//        return ResponseEntity.ok(page);
//    }
//
//    @PutMapping
//    @Transactional
//    public ResponseEntity update(@RequestBody @Valid FieldUpdateDTO data) {
////        var field = fieldRepository.getReferenceById(data.id());
//        var field = fieldService.updateInfo(data);
//
//        return ResponseEntity.ok(field);
//    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) { // route is /doctors/1, for example
//        repository.deleteById(id); // hard delete from database
        var fieldValue = fieldValueRepository.getReferenceById(id);

        if (!fieldValue.getEnabled()) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).header("X-Custom-Message", "Field is already disabled").build();
        }

        fieldValue.deactivate();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{fieldId}")
    public ResponseEntity list(@PathVariable Long fieldId) {

        var list = fieldValueService.findAllValuesByFieldId(fieldId);

        return ResponseEntity.ok(list);
    }
}
