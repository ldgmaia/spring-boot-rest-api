package com.example.api.controller;

import com.example.api.domain.purchaseorders.PurchaseOrderRequestDTO;
import com.example.api.domain.purchaseorders.PurchaseOrderService;
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
@RequestMapping("purchase-orders")
@SecurityRequirement(name = "bearer-key")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

//    @Autowired
//    private FieldRepository fieldRepository;
//
//    @Autowired
//    private FieldGroupRepository fieldGroupRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid PurchaseOrderRequestDTO data, UriComponentsBuilder uriBuilder) {

        var purchaseOrder = purchaseOrderService.register(data);
        var uri = uriBuilder.path("/purchase-orders/{id}").buildAndExpand(purchaseOrder.id()).toUri();

        return ResponseEntity.created(uri).body(purchaseOrder);
    }

//    @GetMapping
//    public ResponseEntity<Page<FieldListDTO>> list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
//        var page = fieldRepository.findAllByEnabledTrue(pagination).map(FieldListDTO::new);
//        return ResponseEntity.ok(page);
//    }
//
//    @PutMapping("/{id}")
//    @Transactional
//    public ResponseEntity update(@RequestBody @Valid FieldUpdateDTO data, @PathVariable Long id) {
////        var field = fieldRepository.getReferenceById(data.id());
//        var field = fieldService.updateInfo(data, id);
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
