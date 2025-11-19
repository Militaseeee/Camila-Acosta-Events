package com.events_cav.events_venues.repository.impl;

import com.events_cav.events_venues.entity.EventEntity;
import com.events_cav.events_venues.repository.interfaces.DataEventRepository;
import com.events_cav.events_venues.repository.interfaces.IEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements IEventRepository {

    private final DataEventRepository jpaRepository;

    @Override
    public EventEntity save(EventEntity event) {
        return jpaRepository.save(event);
    }

    @Override
    public Optional<EventEntity> findById(Long id) {
        return jpaRepository.findById(id);
    }

    // --- AQUÍ ESTÁ EL CAMBIO PARA QUE NO FALLE ---
    @Override
    public Page<EventEntity> findAll(Pageable pageable, String city, LocalDate date) {

        // 1. Inicializamos la Specification SIN usar .where(null) para evitar tu error
        // Esto significa "Traer todo por defecto" (condición siempre verdadera)
        Specification<EventEntity> spec = (root, query, cb) -> cb.conjunction();

        // 2. Filtro 1: Ciudad
        if (city != null && !city.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("venue").get("location")), "%" + city.toLowerCase() + "%"));
        }

        // 3. Filtro 2: Fecha
        if (date != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("date"), date));
        }

        // Ejecutamos. Si esto marca error, es culpa del PASO 2 (DataEventRepository)
        return jpaRepository.findAll(spec, pageable);
    }

    @Override
    public List<EventEntity> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        return jpaRepository.existsByNameAndIdNot(name, id);
    }
}