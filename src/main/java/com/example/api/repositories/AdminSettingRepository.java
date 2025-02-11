package com.example.api.repositories;

import com.example.api.domain.adminsettings.AdminSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminSettingRepository extends JpaRepository<AdminSettings, Long> {
    List<AdminSettings> findByService(String service);
}
