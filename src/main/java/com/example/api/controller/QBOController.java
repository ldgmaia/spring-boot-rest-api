package com.example.api.controller;

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

//    @Autowired
//    private ModelRepository modelRepository;

    @GetMapping("/callback")
    @Transactional
    public ResponseEntity callback(@Valid @ModelAttribute QboCallbackRequestDTO data) { // Using @ModelAttribute to receive QUERY parameters using DTO instead of @RequestParam for each parameter
        return ResponseEntity.ok(data);
    }

    @PostMapping("/webhook")
    public ResponseEntity webhook(@Valid @RequestBody QboWebhookRequestDTO data, @RequestHeader(name = "intuit-signature", required = false) String intuitSignature) {

        return ResponseEntity.ok(intuitSignature);
    }

    @GetMapping("/get-auth-uri")
    public ResponseEntity getAuthUri() {


        return ResponseEntity.ok(qboService.getAuthUri());
    }
}
