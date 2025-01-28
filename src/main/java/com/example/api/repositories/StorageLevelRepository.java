package com.example.api.repositories;

import com.example.api.domain.storage.storagelevel.StorageLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StorageLevelRepository extends JpaRepository<StorageLevel, Long> {
    List<StorageLevel> findAllByStorageLocationId(Long id);

    Optional<StorageLevel> findByStorageLocationIdAndName(Long id, String levelName);

//    String findQrCodeByLevelName(String levelName);

//    String findLevelNameByQrCode(String qrCode);

    //Optional<StorageLevel> findSingleByQrCode(String qrCode);//temp

//    List<StorageLevel> findAllByQrCode(String qrCode);

    //this code is to verify the same Optional<StorageLevel> findSingleByQrCode(String qrCode) but gives the same result
//    @Query("SELECT sl FROM StorageLevel sl WHERE TRIM(LOWER(sl.qrCode)) = TRIM(LOWER(:qrCode))")
//    Optional<StorageLevel> findSingleByQrCode(@Param("qrCode") String qrCode);


//    Optional<StorageLevel> findByCreatedByAndEnabledTrueAndQrCodeIsNotNull(User createdBy);

//    @Query("SELECT COUNT(sl) > 0 FROM StorageLevel sl WHERE sl.id = :levelId AND sl.levelName = :username")
//    boolean existsActiveQRCodeForUser(@Param("levelId") Long id, @Param("username") String username);

    //Optional<StorageLevel> findByQRCode(String qrCode);

    //List<StorageLevel> findByCreatedBy(User createdBy);

    //StorageLevel findByLevelName(String levelName);


//    StorageLevel findByUser(User user);
}
