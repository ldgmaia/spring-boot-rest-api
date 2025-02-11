package com.example.api.repositories;

import com.example.api.domain.gradings.Gradings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradingRepository extends JpaRepository<Gradings, Long> {
    Gradings findByTypeAndScore(String type, Long score);
}
