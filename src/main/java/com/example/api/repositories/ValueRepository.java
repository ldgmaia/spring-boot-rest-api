package com.example.api.repositories;

import com.example.api.domain.values.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValueRepository extends JpaRepository<Value, Long> {

    Value findByValueData(String valueData);

//    Page<Field> findAllByEnabledTrue(Pageable pagination);

//    @Query("""
//            select d from Doctor d
//            where d.active = true
//            and d.specialty = :specialty
//            and d.id not in (
//                select a.doctor.id from Appointment a
//                where a.date = :date
//            )
//            order by rand()
//            limit 1
//            """)
//    Fields pickAvailableDoctor(Specialty specialty, LocalDateTime date);

    //    @Query("""
//            select f.enabled
//            from Field f
//            where f.id = :fieldId
//            """)
//    Boolean findEnabledById(Long fieldId);
//
    Boolean existsByValueData(String valueData);
}
