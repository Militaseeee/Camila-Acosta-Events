package com.events_cav.events_venues.infrastructure.adapters.output.jpa;

import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.EventEntity;
import com.events_cav.events_venues.domain.ports.output.EventRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepositoryPort {

    private final DataEventRepository jpaRepository;

    @Override
    public EventEntity save(EventEntity event) {
        return jpaRepository.save(event);
    }

    @Override
    public Optional<EventEntity> findById(Long id) {
        return jpaRepository.findById(id);
    }

    // Aqui esta el cambio para que no falle
    @Override
    public Page<EventEntity> findAll(Pageable pageable, String city, LocalDate date) {

        // Inicializamos la Specification SIN usar .where(null) para evitar tu error
        // Esto significa "Traer todo por defecto" (condición siempre verdadera)
        Specification<EventEntity> spec = (root, query, cb) -> cb.conjunction();

        // Filtro 1: Ciudad
        if (city != null && !city.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("venue").get("location")), "%" + city.toLowerCase() + "%"));
        }

        // Filtro 2: Fecha
        if (date != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("date"), date));
        }

        // Ejecutamos. Si esto marca error, es culpa del PASO 2 (DataEventRepository)
        return jpaRepository.findAll(spec, pageable);
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