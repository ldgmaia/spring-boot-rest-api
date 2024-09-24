package com.example.api.repositories;

import com.example.api.domain.receivingpictures.ReceivingPicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivingPictureRepository extends JpaRepository<ReceivingPicture, Long> {

}
