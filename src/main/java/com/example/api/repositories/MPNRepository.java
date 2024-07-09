package com.example.api.repositories;

import com.example.api.domain.mpns.MPN;
import com.example.api.domain.mpns.MPNFieldsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MPNRepository extends JpaRepository<MPN, Long> {

    @Query("""
            SELECT NEW com.example.api.domain.mpns.MPNFieldsDTO(f.id, f.name)
            from CategoryField cf
            join cf.field f
            join cf.category c
            join Model m on m.category.id = c.id
            where m.id = :modelId
            and cf.dataLevel = 'MPN'
            """)
    List<MPNFieldsDTO> findMPNFieldsByModelId(Long modelId);
}
