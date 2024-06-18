package com.example.api.controller;

import com.example.api.domain.models.ModelRequestDTO;
import com.example.api.domain.models.ModelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("models")
@SecurityRequirement(name = "bearer-key")
public class ModelsController {

    @Autowired
    private ModelService modelService;

//    @Autowired
//    private FieldRepository fieldRepository;
//
//    @Autowired
//    private FieldGroupRepository fieldGroupRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid ModelRequestDTO data, UriComponentsBuilder uriBuilder) {
//        System.out.println(data.modelFieldsValues());

//        return ResponseEntity.ok().body(data.modelFieldsValues());

//        if (data.fieldGroupId() != null) {
//            var fieldGroupExists = fieldGroupRepository.existsById(data.fieldGroupId());
//
//            if (!fieldGroupExists) {
//                Map<String, String> jsonResponse = Map.of("message", "Field group not found");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
//            }
//        }
//

        var model = modelService.register(data);
        var uri = uriBuilder.path("/models/{id}").buildAndExpand(model.id()).toUri();

        return ResponseEntity.created(uri).body("ok");
    }

//    @GetMapping
//    public ResponseEntity<Page<FieldListDTO>> list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
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
//
//    @DeleteMapping("/{id}")
//    @Transactional
//    public ResponseEntity delete(@PathVariable Long id) { // route is /doctors/1, for example
////        repository.deleteById(id); // hard delete from database
//        var field = fieldRepository.getReferenceById(id);
//
//        if (!field.getEnabled()) {
//            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).header("X-Custom-Message", "Field is already disabled").build();
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
