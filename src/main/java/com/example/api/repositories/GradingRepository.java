package com.example.api.repositories;

import com.example.api.domain.gradings.Gradings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradingRepository extends JpaRepository<Gradings, Long> {
    Gradings findByTypeAndScore(String type, Long score);
}
