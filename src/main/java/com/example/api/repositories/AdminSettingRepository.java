package com.example.api.repositories;

import com.example.api.domain.adminsettings.AdminSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AdminSettingRepository extends JpaRepository<AdminSettings, Long> {
    List<AdminSettings> findByService(String service);

    @Transactional
    @Modifying
    @Query("update AdminSetting a set a.valueParam = ?1 where a.service = ?2 and a.keyParam = ?3")
    void updateValueParamByServiceAndKeyParam(String value_param, String service, String key_param);

    AdminSettings findByServiceAndKeyParam(String service, String keyParam);
}
