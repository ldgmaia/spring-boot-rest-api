package com.example.api.repositories;

import com.example.api.domain.models.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Long> {

//    Page<Field> findAllByEnabledTrue(Pageable pagination);
//
////    Page<Field> findAllByEnabledTrueAndFieldGroup_Id(Long fieldGroupId, Pageable pageable);
//
//    List<Field> findByEnabledTrueAndFieldGroupId(Long fieldGroupId);
////    @Query("""
////            select d from Doctor d
////            where d.active = true
////            and d.specialty = :specialty
////            and d.id not in (
////                select a.doctor.id from Appointment a
////                where a.date = :date
////            )
////            order by rand()
////            limit 1
////            """)
////    Fields pickAvailableDoctor(Specialty specialty, LocalDateTime date);
//
//    @Query("""
//            select f.enabled
//            from Field f
//            where f.id = :fieldId
//            """)
//    Boolean findEnabledById(Long fieldId);
//
//    Boolean existsByName(String name);
//
//    Field findByName(String name);
}
