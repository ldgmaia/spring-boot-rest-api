package com.example.api.repositories;

import com.example.api.domain.adminsettings.AdminSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminSettingRepository extends JpaRepository<AdminSettings, Long> {

}
