package com.example.api.repositories;

import com.example.api.domain.adminsettings.AdminSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminSettingRepository extends JpaRepository<AdminSettings, Long> {

    List<AdminSettings> findByService(String service);
}
