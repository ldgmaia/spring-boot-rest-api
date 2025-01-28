package com.example.api.repositories;

import com.example.api.domain.itemtransfer.AdminSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminSettingsRepository extends JpaRepository<AdminSettings, Long> {

    Optional<AdminSettings> findByKeyAndEnabledTrue(String key);

//    @Query("SELECT CASE WHEN :statusId IN (SELECT CAST(value AS integer) FROM AdminSettings WHERE key = 'prohibited_statuses' AND enabled = true) THEN true ELSE false END")
//    boolean isStatusProhibited(Long statusId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM AdminSettings s WHERE s.prohibitedStatus = :status")
    boolean isStatusProhibited(Long status);
    
}
