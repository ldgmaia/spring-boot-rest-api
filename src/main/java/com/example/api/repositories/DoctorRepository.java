package com.example.api.repositories;

import com.example.api.domain.doctors.Doctor;
import com.example.api.domain.doctors.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Page<Doctor> findAllByActiveTrue(Pageable pagination);

    @Query("""
            select d from Doctor d
            where d.active = true
            and d.specialty = :specialty
            and d.id not in (
                select a.doctor.id from Appointment a
                where a.date = :date
            )
            order by rand()
            limit 1
            """)
    Doctor pickAvailableDoctor(Specialty specialty, LocalDateTime date);

    @Query("""
            select d.active
            from Doctor d
            where d.id = :doctorId
            """)
    Boolean findActiveById(Long doctorId);

    Boolean existsByName(String name);
}
