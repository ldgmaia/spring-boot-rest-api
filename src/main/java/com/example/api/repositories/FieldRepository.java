package com.example.api.repositories;

import com.example.api.domain.fields.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FieldRepository extends JpaRepository<Field, Long> {

    Page<Field> findAllByEnabledTrue(Pageable pagination);

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

    @Query("""
            select f.enabled
            from Field f
            where f.id = :fieldId
            """)
    Boolean findEnabledById(Long fieldId);

    Boolean existsByName(String name);
}
