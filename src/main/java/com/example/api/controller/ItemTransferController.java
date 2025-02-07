//package com.example.api.controller;
//
//import com.example.api.domain.itemtransfer.ItemTransferRequestDTO;
//import com.example.api.domain.itemtransfer.ItemTransferResponseDTO;
//import com.example.api.domain.itemtransfer.ItemTransferService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/warehouse/transfers")
//@SecurityRequirement(name = "bearer-key")
//public class ItemTransferController {
//
//    @Autowired
//    private ItemTransferService itemTransferService;
//
//    @PutMapping("/process")
//    public ResponseEntity<ItemTransferResponseDTO> processTransfer(@RequestBody @Valid ItemTransferRequestDTO request) {
//        System.err.println("controller request: " + request);
//        ItemTransferResponseDTO response = itemTransferService.processTransfer(request);
//        return ResponseEntity.ok(response);
//    }
//
//}
