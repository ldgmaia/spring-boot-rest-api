package com.example.api.controller;

import com.example.api.domain.models.ModelInfoDTO;
import com.example.api.domain.models.ModelRequestDTO;
import com.example.api.domain.models.ModelService;
import com.example.api.domain.models.ModelUpdateDTO;
import com.example.api.repositories.ModelRepository;
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
@RequestMapping("models")
@SecurityRequirement(name = "bearer-key")
public class ModelsController {

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelRepository modelRepository;

//    @Autowired
//    private FieldGroupRepository fieldGroupRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid ModelRequestDTO data, UriComponentsBuilder uriBuilder) {

        var model = modelService.register(data);
        var uri = uriBuilder.path("/models/{id}").buildAndExpand(model.id()).toUri();

        return ResponseEntity.created(uri).body(model);
    }

    @GetMapping
    public ResponseEntity<Page<ModelInfoDTO>> list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = modelRepository.findAll(pagination).map(ModelInfoDTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity update(@RequestBody @Valid ModelUpdateDTO data, @PathVariable Long id) {
        var field = modelService.update(data, id);
        return ResponseEntity.ok(field);
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {

        var modelDetails = modelService.getModelDetails(id);

        return ResponseEntity.ok(modelDetails);

    }

    @GetMapping("/list/needs-mpn")
    public ResponseEntity listModelsNeedsMpn() {
        var models = modelRepository.findAllByNeedsMpnTrue();
        return ResponseEntity.ok(models);
    }

}
