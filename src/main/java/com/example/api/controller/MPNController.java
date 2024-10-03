package com.example.api.controller;

import com.example.api.domain.mpns.MPNRequestDTO;
import com.example.api.domain.mpns.MPNService;
import com.example.api.repositories.MPNFieldValueRepository;
import com.example.api.repositories.MPNRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MPNRepository mpnRepository;

    @Autowired
    private MPNFieldValueRepository mpnFieldValueRepository;

    @Autowired
    private MPNService mpnService;
    private Long modelId;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid MPNRequestDTO data, UriComponentsBuilder uriBuilder) {

        var mpn = mpnService.register(data);
        var uri = uriBuilder.path("/mpns/{id}").buildAndExpand(mpn.id()).toUri();
        return ResponseEntity.created(uri).body(mpn);
    }

    @GetMapping("/list/mpn-fields/{modelId}")
    public ResponseEntity listMpnFieldsByModel(@PathVariable Long modelId) {
        var mpnFields = mpnService.listMpnFields(modelId);
        return ResponseEntity.ok(mpnFields);
    }

    @GetMapping
    public ResponseEntity list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = mpnRepository.listAllMPN(pagination);
        return ResponseEntity.ok(page);
//        var page = mpnRepository.findAll(pagination).map(MPNInfoDTO::new);
//        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        var mpnDetails = mpnService.getMpnDetails(id);

        return ResponseEntity.ok(mpnDetails);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity update(@RequestBody @Valid MPNRequestDTO data, @PathVariable Long id) {
        var mpn = mpnService.update(data, id);
        return ResponseEntity.ok(mpn);
    }

    @GetMapping("/get-by-modelid/{modelId}")
    public ResponseEntity modelByCategoryId(@PathVariable Long modelId) {
        var modelDetails = mpnService.getMpnByModelId(modelId);
        return ResponseEntity.ok(modelDetails);
    }
}
