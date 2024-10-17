package com.example.api.repositories;

import com.example.api.domain.assessments.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    // Check for any methods that might reference `assessment_item`


//    @Query("""
//            SELECT new com.example.api.domain.assessments.PendingAssessmentListDTO(
//                ri.id, po.poNumber, ri.description, ri.status, poi.quantityOrdered, ri.quantityReceived, s.name, ri.createdAt
//            )
//            FROM ReceivingItem ri
//            JOIN ri.purchaseOrderItem poi
//            JOIN poi.purchaseOrder po
//            JOIN po.supplier s
//            JOIN ri.receiving r
//            WHERE r.type = 'PO'
//            AND ri.quantityReceived > 0
//            AND ri.status = :statusToFind
//            """)
//    Page<PendingAssessmentListDTO> findItemsByStatus(@Param("statusToFind") String statusToFind, Pageable pageable);
}

//@Repository
//public interface AssessmentRepository extends JpaRepository<AssessmentItem, Long> {

//    @Query("""
//            SELECT new com.example.api.domain.assessments.PendingAssessmentListDTO(
//                ri.id, po.poNumber, ri.description, ri.status, poi.quantityOrdered, ri.quantityReceived, s.name, ri.createdAt
//            )
//            FROM ReceivingItem ri
//                JOIN ri.purchaseOrderItem poi
//                JOIN poi.purchaseOrder po
//                JOIN po.supplier s
//                JOIN ri.receiving r
//            WHERE r.type = 'PO'
//                AND ri.quantityReceived > 0
//                AND ri.status = :statusToFind
//            """)
//    List<PendingAssessmentListDTO> findItemsByStatus(@Param("statusToFind") String statusToFind);


//}


//    @Query("""
//            SELECT new com.example.api.domain.assessments.PendingAssessmentListDTO(
//                ai.id, po.poNumber, ai.description, ai.status, poi.quantityOrdered, ai.quantityReceived, s.name, ai.createdAt
//            )
//            FROM AssessmentItem ai
//                JOIN ai.receiving r
//                JOIN r.purchaseOrder po
//                JOIN ai.purchaseOrderItem poi
//                JOIN po.supplier s
//            WHERE r.type = 'PO'
//                AND ai.quantityReceived > 0
//                AND ai.status = :status
//            """)
//    List<PendingAssessmentListDTO> findItemsByStatus(@Param("status") String status);
//}

//@Repository
//public interface AssessmentRepository extends JpaRepository<AssessmentItem, Long> {
//
//    @Query("""
//            SELECT new com.example.api.domain.assessments.PendingAssessmentListDTO(
//                ri.id, po.poNumber, ri.description, ri.status, poi.quantityOrdered, ri.quantityReceived, s.name, ri.createdAt
//            )
//            FROM ReceivingItem ri
//                JOIN ri.receiving r
//                JOIN r.purchaseOrder po
//                JOIN ri.purchaseOrderItem poi
//                JOIN po.supplier s
//            WHERE r.type = 'PO'
//                AND ri.quantityReceived > 0
//                AND ri.status = :status
//            """)
//    List<PendingAssessmentListDTO> findItemsByStatus(@Param("status") String status);
//
//}
