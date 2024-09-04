package com.example.api.repositories;

import com.example.api.domain.files.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    
}
