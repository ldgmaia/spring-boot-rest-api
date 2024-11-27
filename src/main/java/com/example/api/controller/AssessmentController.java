package com.example.api.controller;

import com.example.api.domain.assessments.AssessmentRequestDTO;
import com.example.api.domain.assessments.AssessmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("assessments")
@SecurityRequirement(name = "bearer-key")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

//    @GetMapping("/list/spec-func-fields/{inventoryItemId}")
//    public ResponseEntity list(@PathVariable Long inventoryItemId) {
//        var specFuncFields = assessmentService.findSpecFuncFieldsByInventoryItemId(inventoryItemId);

    /// /        AssessmentListSpecFuncFieldsDTO specFuncFields = assessmentService.findSpecFuncFieldsByInventoryItemId(inventoryItemId);
//        return ResponseEntity.ok(specFuncFields);
//    }
    @PostMapping
    @Transactional
    public ResponseEntity createAssessment(@RequestBody @Valid AssessmentRequestDTO data) {
        assessmentService.createAssessment(data);
        return ResponseEntity.ok("OK");
    }

//    @PostMapping("/save/draft")
//    public ResponseEntity saveDraftAssessment(@RequestBody AssessmentRequestDTO request) {
//        assessmentService.saveDraftAssessment(request);
//        return ResponseEntity.ok("OK");
//    }

//    @GetMapping
//    public ResponseEntity<List<Assessment>> getAllAssessments() {
//        List<Assessment> assessments = assessmentService.getAllAssessments();
//        return ResponseEntity.ok(assessments);
//    }
}
