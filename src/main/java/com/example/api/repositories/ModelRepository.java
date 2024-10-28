package com.example.api.repositories;

import com.example.api.domain.models.Model;
import com.example.api.domain.models.ModelComponentInfoDTO;
import com.example.api.domain.models.ModelListNeedsMpnDTO;
import com.example.api.domain.models.ModelsByCategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModelRepository extends JpaRepository<Model, Long> {

    @Query("""
            select new com.example.api.domain.models.ModelListNeedsMpnDTO(m.id, m.name) from Model m
            where m.enabled = true
            and m.needsMpn = true
            order by m.name
            """)
    List<ModelListNeedsMpnDTO> findAllByNeedsMpnTrue();

    @Query("""
            select m
            from Model m
            join m.category c
            where c.id not in (
            	select cc.parentCategory.id from CategoryComponent cc
            	)
            """)
    List<ModelComponentInfoDTO> listModelComponent();

    List<ModelsByCategoryDTO> findAllByCategoryId(Long categoryId);
}
