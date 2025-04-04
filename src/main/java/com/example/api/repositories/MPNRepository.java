package com.example.api.repositories;

import com.example.api.domain.mpns.MPN;
import com.example.api.domain.mpns.MPNFieldsDTO;
import com.example.api.domain.mpns.MPNInfoListDTO;
import com.example.api.domain.mpns.MPNsByModelDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MPNRepository extends JpaRepository<MPN, Long> {

    @Query("""
            SELECT NEW com.example.api.domain.mpns.MPNFieldsDTO(f.id, f.name, f.dataType)
            from CategoryField cf
            join cf.field f
            join cf.category c
            join Model m on m.category.id = c.id
            where m.id = :modelId
            and cf.dataLevel = 'MPN'
            and cf.enabled = true
            order by f.id
            """)
    List<MPNFieldsDTO> findMPNFieldsByModelId(Long modelId);

    @Query("""
            SELECT NEW com.example.api.domain.mpns.MPNInfoListDTO(
                m.id, m.name, mod.name, m.status, m.enabled, createdBy.firstName, approvedBy.firstName)
            FROM MPN m
            JOIN m.model mod
            LEFT JOIN m.createdBy createdBy
            LEFT JOIN m.approvedBy approvedBy
            """)
    Page<MPNInfoListDTO> listAllMPN(Pageable pageable);

    @Query("""
            SELECT NEW com.example.api.domain.mpns.MPNInfoListDTO(
                m.id, m.name, mod.name, m.status, m.enabled, createdBy.firstName, approvedBy.firstName)
            FROM MPN m
            JOIN m.model mod
            LEFT JOIN m.createdBy createdBy
            LEFT JOIN m.approvedBy approvedBy
            WHERE m.id = :id
            """)
    MPNInfoListDTO getMpnDetails(Long id);

    List<MPNsByModelDTO> findAllByModelId(Long modelId);
}
