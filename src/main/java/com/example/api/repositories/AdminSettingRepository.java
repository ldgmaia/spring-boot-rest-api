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
    @Query("update AdminSetting a set a.value_param = ?1 where a.service = ?2 and a.key_param = ?3")
    void updateValueparamByServiceAndKeyparam(String value_param, String service, String key_param);


}
