package com.example.api.domain.patients;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Page<Patient> findAllByActiveTrue(Pageable pagination);

    @Query("""
            select p.active
            from Patient p
            where p.id = :patientId
            """)
//    @Query(
//        value = """
//                select *
//                from patients p
//                where p.id = 1
//                """,
//        nativeQuery = true)
    Boolean findActiveById(Long patientId);
}
