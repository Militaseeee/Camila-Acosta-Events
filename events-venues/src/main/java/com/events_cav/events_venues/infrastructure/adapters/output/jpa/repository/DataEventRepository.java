package com.events_cav.events_venues.infrastructure.adapters.output.jpa.repository;

import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface DataEventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    // optimización N+1 (join fetch)

    // Lectura por ID -> JPQL con JOIN FETCH para cargar el Venue en una sola consulta
    @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue WHERE e.id = :id")
    Optional<EventEntity> findByIdWithVenue(@Param("id") Long id);

    // Lectura Paginada (Para el GET ALL sin filtros específicos)
    // También usa JOIN FETCH para la optimización N+1 en listados
    @Query(value = "SELECT e FROM EventEntity e JOIN FETCH e.venue",
            countQuery = "SELECT count(e) FROM EventEntity e")
    Page<EventEntity> findAllWithVenue(Pageable pageable);

}