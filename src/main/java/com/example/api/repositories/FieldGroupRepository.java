package com.example.api.repositories;

import com.example.api.domain.fieldgroups.FieldGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldGroupRepository extends JpaRepository<FieldGroup, Long> {

    Page<FieldGroup> findAllByEnabledTrue(Pageable pagination);

    Boolean existsByName(String name);

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
//    Doctor pickAvailableDoctor(Specialty specialty, LocalDateTime date);

    @Query("""
            select fg.enabled
            from FieldGroup fg
            where fg.id = :fieldGroupId
            """)
    Boolean findEnabledById(Long fieldGroupId);
}
