package com.example.api.repositories;

import com.example.api.domain.inventoryitemscomponents.InventoryItemComponents;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryItemComponentRepository extends JpaRepository<InventoryItemComponents, Long> {

    List<InventoryItemComponents> findByParentInventoryItemId(Long id);

//    @Query("""
//            update InventoryItemComponent iic
//            set iic.parentInventoryItem = null
//            where iic.parentInventoryItem = :parentInventoryItemId
//            """)
//    void removeParentItemFromComponentsByParentInventoryItemId(@Param("parentInventoryItemId") Long parentInventoryItemId);

    // create query to set parentInventoryItem equal to null by parentInventoryItem

    @Modifying
    @Transactional
    @Query("""
            update InventoryItemComponent iic
            set iic.parentInventoryItem = null
            where iic.parentInventoryItem.id = :parentInventoryItemId
            """)
    void removeParentItemOfComponents(@Param("parentInventoryItemId") Long parentInventoryItemId);

    @Modifying
    @Transactional
    @Query("""
            update InventoryItem ii
            set ii.present = false, ii.itemStatus.id = 2
            where ii.id in (
                select iic.inventoryItem.id
                from InventoryItemComponent iic
                where iic.parentInventoryItem.id = :parentInventoryItemId
            )
            """)
    void updateInventoryItemsPresenceToFalse(@Param("parentInventoryItemId") Long parentInventoryItemId);

//    @Modifying
//    @Transactional
//    @Query("""
//            update InventoryItemComponent iic
//            set iic.parentInventoryItem = :parentInventoryItemId
//            where iic.inventoryItem.id in :inventoryItemIds
//            """)
//    void addParentItemToComponents(@Param("parentInventoryItemId") Long parentInventoryItemId, @Param("inventoryItemIds") List<Long> inventoryItemIds);

    @Modifying
    @Transactional
    @Query("""
            update InventoryItemComponent iic
            set iic.parentInventoryItem.id = :parentInventoryItemId
            where iic.inventoryItem.id in :inventoryItemIds
            """)
    void addParentItemToComponents(@Param("parentInventoryItemId") Long parentInventoryItemId, @Param("inventoryItemIds") List<Long> inventoryItemIds);


}
