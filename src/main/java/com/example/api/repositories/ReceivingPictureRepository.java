package com.example.api.repositories;

import com.example.api.domain.receivingpictures.ReceivingPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivingPictureRepository extends JpaRepository<ReceivingPicture, Long> {

}
