package com.example.api.repositories;

import com.example.api.domain.receivings.Receiving;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReceivingRepository extends JpaRepository<Receiving, Long> {

    @Query("""
            SELECT r
            FROM Receiving r
            LEFT JOIN FETCH r.pictures
            """)
    Page<Receiving> findAllWithPictures(Pageable pageable);
}
