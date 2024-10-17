package com.example.api.domain.assessments;


import com.example.api.repositories.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

//    public Page<PendingAssessmentListDTO> getItemsByStatus(String statusToFind, Pageable pageable) {
//        System.err.println("2. getItemsByStatus - statusToFind: " + statusToFind);
//
//        Page<PendingAssessmentListDTO> assessmentByStatus = assessmentRepository.findItemsByStatus(pageable);
//        if (assessmentByStatus.isEmpty()) {
//            throw new ValidationException("Assessment items with statusToFind " + statusToFind + " not found");
//        }
//        return assessmentByStatus;
//    }
}

//
//@Service
//public class AssessmentService {
//
//    @Autowired
//    private AssessmentRepository assessmentRepository;
//
//    public List<PendingAssessmentListDTO> getItemsByStatus(String status) {
//        System.out.println("getItemsByStatus - status: " + status);
//
//        List<PendingAssessmentListDTO> assessmentByStatus = assessmentRepository.findItemsByStatus(status);
//        if (assessmentByStatus.isEmpty()) {
//            throw new ValidationException("Assessment items with status " + status + " not found");
//        }
//        return assessmentByStatus;
//    }
//
//}
