package com.example.api.controller;

import com.example.api.domain.mpnfieldsvalues.MPNFieldValueInfoDTO;
import com.example.api.domain.mpns.MPNInfoListDTO;
import com.example.api.domain.mpns.MPNRequestDTO;
import com.example.api.domain.mpns.MPNService;
import com.example.api.repositories.MPNFieldValueRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("mpns")
@SecurityRequirement(name = "bearer-key")
public class MPNController {

    @Autowired
    private MPNRepository mpnRepository;

    @Autowired
    private MPNFieldValueRepository mpnFieldValueRepository;

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
        return ResponseEntity.created(uri).body(mpn);
//        return ResponseEntity.ok(data);
    }

    @GetMapping("/list/mpn-fields/{modelId}")
    public ResponseEntity listMpnFieldsByModel(@PathVariable Long modelId) {
        var mpnFields = mpnService.listMpnFields(modelId);
        return ResponseEntity.ok(mpnFields);
    }

    @GetMapping
    public ResponseEntity<Page<MPNInfoListDTO>> list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = mpnRepository.findAll(pagination);
        Page<MPNInfoListDTO> dtoPage = page.map(mpn -> {
            List<MPNFieldValueInfoDTO> mpnFieldsValues = mpnFieldValueRepository.findAllByMpnId(mpn.getId()).stream()
                    .map(MPNFieldValueInfoDTO::new)
                    .collect(Collectors.toList());
            return new MPNInfoListDTO(
                    mpn.getId(),
                    mpn.getName(),
                    mpn.getModel().getName(),
                    mpn.getStatus(),
                    mpn.getEnabled(),
                    mpn.getCreatedBy() != null ? mpn.getCreatedBy().getFirstName() : null,
                    mpn.getApprovedBy() != null ? mpn.getApprovedBy().getFirstName() : null,
                    mpnFieldsValues
            );
        });
        return ResponseEntity.ok(dtoPage);
//        var page = mpnRepository.listAllMPN(pagination);
//        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        var mpnExists = mpnRepository.existsById(id);

        if (!mpnExists) {
            Map<String, String> jsonResponse = Map.of("message", "MPN not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
        }

        var mpn = mpnService.get(id);
        return ResponseEntity.ok(mpn);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity update(@RequestBody @Valid MPNRequestDTO data, @PathVariable Long id) {
        var mpn = mpnService.update(data, id);
        return ResponseEntity.ok(mpn);
    }

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
}
