//package com.example.api.controller;
//
//import com.example.api.domain.assessments.AssessmentService;
//import com.example.api.domain.assessments.PendingAssessmentListDTO;
//import com.example.api.repositories.AssessmentRepository;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.data.web.PagedResourcesAssembler;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("assessments")
//@SecurityRequirement(name = "bearer-key")
//public class AssessmentController {
//
//    @Autowired
//    private AssessmentRepository assessmentRepository;
//
//    @Autowired
//    private AssessmentService assessmentService;
//
//    @Autowired
//    private PagedResourcesAssembler<PendingAssessmentListDTO> pagedResourcesAssembler;
//
//
//    @GetMapping
//    public ResponseEntity<Page<PendingAssessmentListDTO>> list(
//            HttpServletRequest request,
//            @PageableDefault(size = 100, page = 0, sort = {"id"}) Pageable pagination,
//            @RequestHeader HttpHeaders headers) {
//
//        System.err.println("1. pagination: " + pagination);
//
//        // Modify the service to handle pagination
//        Page<PendingAssessmentListDTO> listPendingAssessments = assessmentService.getItemsByStatus("Pending assessment", pagination);
//
//        // Return the page result
//        return ResponseEntity.ok(listPendingAssessments);
//    }
//
////    @GetMapping("/{status}")
////    public ResponseEntity detail(@PathVariable String status, @PageableDefault(size = 100, page = 0, sort = {"id"}) Pageable pagination) {
////        var receivingItem = assessmentService.getItemsByStatus(status, pagination);
////
////        return ResponseEntity.ok(receivingItem);
////    }
//
//}
//
////@GetMapping
////public ResponseEntity<Page<PendingAssessmentListDTO>> list
////        (HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"id"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
////    var page = assessmentRepository.findAll(pagination)
////            .map(PendingAssessmentListDTO::new);
////    return ResponseEntity.ok(page);
////}
//
////    @GetMapping("/get-items-by-status/{statusToFind}")
////    public ResponseEntity getItemsByStatus(@RequestParam String statusToFind) {//@PathVariable
////        System.err.println("1. statusToFind received in controller: " + statusToFind);
////        var itemsByStatus = assessmentService.getItemsByStatus(statusToFind);
////        return ResponseEntity.ok(itemsByStatus);
////    }
////
////    @GetMapping("/{statusToFind}")//@PathVariable
////    public ResponseEntity list(@RequestParam String statusToFind) {
////
////        var list = assessmentService.getItemsByStatus(statusToFind);
////
////        return ResponseEntity.ok(list);
////    }
//
//
////@RestController
////@RequestMapping("assessments")
////public class AssessmentController {
////
////    @Autowired
////    AssessmentService assessmentService;
////
////    @GetMapping("/status")
////    public ResponseEntity<List<PendingAssessmentListDTO>> detail(@RequestParam String status) {
////        List<PendingAssessmentListDTO> assessmentItems = assessmentService.getItemsByStatus(status);
////        return ResponseEntity.ok(assessmentItems);
////    }
////
////}
//
//
////@RequestMapping("/error")
////public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
////    Map<String, Object> body = new HashMap<>();
////    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
////    Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
////    Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
////
////    body.put("status", status);
////    body.put("message", message);
////    if (exception != null) {
////        body.put("exception", exception.toString());
////    }
////    return new ResponseEntity<>(body, HttpStatus.valueOf((Integer) status));
////}
//
//
////    @GetMapping("/status/{status}")
////    @GetMapping("/status")
////    //@RequestParam String status
////    public ResponseEntity<List<PendingAssessmentListDTO>> getItemsByStatus(@RequestParam String status) {
////        //System.err.println("status: " + status);
////        //var status = "Pending assessment";
////        System.err.println("1. status received in controller: " + status);
////        List<PendingAssessmentListDTO> assessmentItems = assessmentService.getItemsByStatus(status);
////        return ResponseEntity.ok(assessmentItems);
////    }
