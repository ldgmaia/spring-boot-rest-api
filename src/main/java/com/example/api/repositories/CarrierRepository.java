package com.example.api.repositories;

import com.example.api.domain.carriers.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarrierRepository extends JpaRepository<Carrier, Long> {

    List<Carrier> findByEnabled(Boolean enabled);
}
