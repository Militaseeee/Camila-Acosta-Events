package com.events_cav.events_venues.infrastructure.adapters.output.jpa.repository;

import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DataEventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

}