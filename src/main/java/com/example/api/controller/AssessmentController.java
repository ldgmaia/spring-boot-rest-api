package com.example.api.controller;

import com.example.api.domain.assessments.PendingAssessmentListDTO;
import com.example.api.repositories.ReceivingItemRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("assessments")
@SecurityRequirement(name = "bearer-key")
public class AssessmentController {

    @Autowired
    private ReceivingItemRepository receivingItemRepository;

    @GetMapping
    public ResponseEntity<Page<PendingAssessmentListDTO>> list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"status"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
//        List<PendingAssessmentListDTO> pendingAssessments = assessmentRepository.findItemsByStatus("Pending assessment");

        //Page<PendingAssessmentListDTO> pendingAssessments = receivingItemRepository.findItemsByStatus(statusToFind, pagination);
        Page<PendingAssessmentListDTO> pendingAssessments = receivingItemRepository.findItemsByStatus("Pending assessment", pagination);
        return ResponseEntity.ok(pendingAssessments);

//        var listPendingAssessments = assessmentService.getItemsByStatus("Pending assessment", pagination);
//        return ResponseEntity.ok(listPendingAssessments);
    }

}
