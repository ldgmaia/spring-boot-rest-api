//package com.example.api.repositories;
//
//// src/main/java/com/example/api/repositories/InventoryItemsRepository.java
//
//import com.example.api.domain.assessments.PendingAssessmentListDTO;
//import com.example.api.domain.inventoryitems.InventoryItem;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface InventoryItemsRepository extends JpaRepository<InventoryItem, Long> {
//
//    @Query("""
//            select new com.example.api.domain.assessments.PendingAssessmentListDTO(
//                ri.id, po.poNumber, ri.description, ri.status, poi.quantityOrdered,
//                ri.quantityReceived, s.name, ri.createdAt)
//            from ReceivingItem ri
//            join ri.receiving r
//            join r.purchaseOrder po
//            join ri.purchaseOrderItem poi
//            join po.supplier s
//            where r.type = 'PO'
//            and ri.quantityReceived > 0
//            and ri.status = :status
//            """)
//    List<PendingAssessmentListDTO> findItemsByStatus(@Param("status") String status);
//}
