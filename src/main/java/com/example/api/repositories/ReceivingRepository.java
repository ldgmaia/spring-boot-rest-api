package com.example.api.repositories;

import com.example.api.domain.receivings.Receiving;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivingRepository extends JpaRepository<Receiving, Long> {

}
