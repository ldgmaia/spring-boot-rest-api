package com.example.api.controller;

import com.example.api.domain.purchaseorders.PurchaseOrderResponseDTO;
import com.example.api.domain.settings.qbo.QboCallbackRequestDTO;
import com.example.api.domain.settings.qbo.QboService;
import com.example.api.domain.settings.qbo.QboWebhookRequestDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("qbo")
@SecurityRequirement(name = "bearer-key")
public class QBOController {

    @Autowired
    private QboService qboService;

    @GetMapping("/callback")
    @Transactional
    public ResponseEntity callback(@Valid @ModelAttribute QboCallbackRequestDTO data) { // Using @ModelAttribute to receive QUERY parameters using DTO instead of @RequestParam for each parameter
        return ResponseEntity.ok(qboService.handleCallback(data));
    }

    @PostMapping("/webhook")
    public void webhook(@Valid @RequestBody QboWebhookRequestDTO data, @RequestHeader(name = "intuit-signature", required = false) String intuitSignature) {
        Long qboPurchaseOrderId = Long.valueOf(data.eventNotifications().get(0).dataChangeEvent().entities().get(0).id());

        switch (data.eventNotifications().get(0).dataChangeEvent().entities().get(0).operation()) {
            case "Create": {
                String urlModule = "/purchaseorder/" + qboPurchaseOrderId;
                PurchaseOrderResponseDTO po = qboService.fetchFromQbo(urlModule, PurchaseOrderResponseDTO.class);
                qboService.createPurchaseOrder(po);
                break;
            }
            case "Update": {
                String urlModule = "/purchaseorder/" + qboPurchaseOrderId;
                PurchaseOrderResponseDTO po = qboService.fetchFromQbo(urlModule, PurchaseOrderResponseDTO.class);
                qboService.updatePurchaseOrder(po);
                break;
            }
            case "Delete":
                qboService.deletePurchaseOrder(qboPurchaseOrderId);
                break;
            default:
                System.out.println("Operation not supported");
        }
    }

    @GetMapping("/get-auth-uri")
    public ResponseEntity getAuthUri() {
        return ResponseEntity.ok(qboService.getAuthUri());
    }

    @GetMapping("/get-purchase-order-by-id/{purchaseOrderId}")
    public ResponseEntity getPurchaseOrder(@PathVariable Long purchaseOrderId) {
        String urlModule = "/purchaseorder/" + purchaseOrderId;
        return ResponseEntity.ok(qboService.fetchFromQbo(urlModule, PurchaseOrderResponseDTO.class));
    }

    @GetMapping("/get-purchase-order-by-id/{purchaseOrderId}/pdf")
    public ResponseEntity getPurchaseOrderPdf(@PathVariable Long purchaseOrderId) {
        String urlModule = "/purchaseorder/" + purchaseOrderId + "/pdf";
        return ResponseEntity.ok(qboService.fetchFromQbo(urlModule, byte[].class));
    }
}
