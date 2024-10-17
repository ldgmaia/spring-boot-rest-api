package com.example.api.repositories;

import com.example.api.domain.itemcondition.ItemCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemConditionRepository extends JpaRepository<ItemCondition, Long> {

}
