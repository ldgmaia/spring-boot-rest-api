package com.example.api.controller;

import com.example.api.domain.mpns.MPNInfoListDTO;
import com.example.api.domain.mpns.MPNRequestDTO;
import com.example.api.domain.mpns.MPNService;
import com.example.api.repositories.MPNRepository;
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
@RequestMapping("mpns")
@SecurityRequirement(name = "bearer-key")
public class MPNController {

//    @Autowired
//    private ModelService modelService;

    @Autowired
    private MPNRepository mpnRepository;

    @Autowired
    private MPNService mpnService;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid MPNRequestDTO data, UriComponentsBuilder uriBuilder) {
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

        var mpn = mpnService.register(data);
        var uri = uriBuilder.path("/mpns/{id}").buildAndExpand(mpn.id()).toUri();

        return ResponseEntity.ok(data);
    }

    @GetMapping("/list/mpn-fields/{modelId}")
    public ResponseEntity detail(@PathVariable Long modelId) {
        var mpnFields = mpnService.listMpnFields(modelId);
        return ResponseEntity.ok(mpnFields);
    }

    @GetMapping
    public ResponseEntity<Page<MPNInfoListDTO>> list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = mpnRepository.listAllMPN(pagination);
        return ResponseEntity.ok(page);
    }

//    @PutMapping("/{id}")
//    @Transactional
//    public ResponseEntity update(@RequestBody @Valid ModelUpdateDTO data, @PathVariable Long id) {
//        var field = modelService.update(data, id);
//        return ResponseEntity.ok(field);
//    }

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


//    @GetMapping("/list/mpn-fields/{modelId}")
//    public ResponseEntity<List<MPNFieldsDTO>> listMpnFields(@PathVariable Long modelId) {
//
////        modelRepository.findById(id)
////                .orElseThrow(() -> new ValidationException("Model not found"));
//        var mpnFields = mpnService.listMpnFields(modelId);
//        return ResponseEntity.ok(mpnFields);
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity detail(@PathVariable Long id) {
//
////        modelRepository.findById(id)
////                .orElseThrow(() -> new ValidationException("Model not found"));
//
//        var modelDetails = modelService.getModelDetails(id);
//
//        return ResponseEntity.ok(modelDetails);
////        try {
////            var model = modelRepository.getReferenceById(id);
////            return ResponseEntity.ok(new ModelInfoDTO(model));
////        } catch (EntityNotFoundException ex) {
////            Map<String, String> jsonResponse = Map.of("message", "Model not found");
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
////        }
//    }

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
