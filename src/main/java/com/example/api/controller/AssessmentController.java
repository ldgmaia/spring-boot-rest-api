//package com.example.api.controller;
//
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("assessments")
//@SecurityRequirement(name = "bearer-key")
//public class AssessmentController {
//
////    @Autowired
////    private ReceivingItemRepository receivingItemRepository;
////
////    @Autowired
////    private ReceivingItemService receivingItemService;
//
//
////    @GetMapping
////    public ResponseEntity<Page<PendingAssessmentListDTO>> list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"createdAt"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
////        Page<PendingAssessmentListDTO> pendingAssessments = receivingItemRepository.findReceivingsByStatus("Pending assessment", pagination);
////        return ResponseEntity.ok(pendingAssessments);
////    }
//
////    @GetMapping("/by-po-number/{poNumber}")
////    //public ResponseEntity<List<ReceivingAssessmentListDTO>> list(@PathVariable String poNumber, HttpServletRequest request, @RequestHeader HttpHeaders headers) {
////    public ResponseEntity<List<ReceivingAssessmentListDTO>> list(@PathVariable String poNumber, @PageableDefault(size = 100, page = 0, sort = {"createdAt"}) Pageable pagination) {
////        List<ReceivingAssessmentListDTO> receivingItemsbyPoNumber = receivingItemRepository.findReceivedItemsByPurchaseOrderNumber(poNumber);
////
////        return ResponseEntity.ok(receivingItemsbyPoNumber);
////    }
//
//
////    @GetMapping("/by-serial-number/{serialNumber}")
////    public ResponseEntity listBySerialNumber(@PathVariable String serialNumber) {
////        List<ReceivingAssessmentListDTO> itemsBySerialNumber = receivingItemRepository.findReceivedItemsBySerialNumber(serialNumber);
////        return ResponseEntity.ok(itemsBySerialNumber);
////    }
//
////    @GetMapping("/by-receiving-id/{receivingId}")
////    public ResponseEntity listByReceivingId(@PathVariable String receivingId) {
////        List<ReceivingAssessmentListDTO> itemsByReceivingId = receivingItemRepository.findReceivedItemsByReceivingId(receivingId);
////        return ResponseEntity.ok(itemsByReceivingId);
////    }
//
////    @GetMapping("/by-description/{description}")
////    public ResponseEntity listByDescription(@PathVariable String description) {
////        List<ReceivingAssessmentListDTO> itemsByDescription = receivingItemRepository.findReceivedItemsByDescription(description);
////        return ResponseEntity.ok(itemsByDescription);
////    }
//
////    @GetMapping("/by-tracking-lading/{trackingLading}")
////    public ResponseEntity listByTrakingLading(@PathVariable String trackingLading) {
////        List<ReceivingAssessmentListDTO> itemsByTrakingLading = receivingItemRepository.findReceivedItemsByTrackinglading(trackingLading);
////        return ResponseEntity.ok(itemsByTrakingLading);
////    }
//
////    @GetMapping("/by-supplier-id/{supplierId}")
////    public ResponseEntity listBySupplierId(@PathVariable String supplierId) {
////        List<ReceivingAssessmentListDTO> itemsBySupplierId = receivingItemRepository.findReceivedItemsBySupplierId(supplierId);
////        return ResponseEntity.ok(itemsBySupplierId);
////    }
//
////    @GetMapping("/list-with-filter/{filter}")
////    public ResponseEntity listByFilter(@PathVariable String filter) {
////        List<ReceivingAssessmentListDTO> itemsByFilter = receivingItemRepository.findReceivedItemsByFilter(filter);
////        return ResponseEntity.ok(itemsByFilter);
////    }
//
//}
