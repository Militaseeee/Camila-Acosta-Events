package com.events_cav.events_venues.infrastructure.adapters.output.jpa.adapter;

import com.events_cav.events_venues.domain.model.EventModel; // 🔴 AHORA USAMOS EL MODELO
import com.events_cav.events_venues.domain.ports.output.EventRepositoryPort;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.repository.DataEventRepository;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.EventEntity;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper.EventMapper; // 🔴 Usamos el Mapper aquí
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventJpaAdapter implements EventRepositoryPort {

    private final DataEventRepository jpaRepository;
    private final EventMapper eventMapper = EventMapper.INSTANCE; // Instanciamos el Mapper

    // El Puerto recibe MODEL y devuelve MODEL
    @Override
    public EventModel save(EventModel event) {
        // Model -> Entity
        EventEntity entityToSave = eventMapper.toEventEntity(event);

        // Guardar Entity
        EventEntity savedEntity = jpaRepository.save(entityToSave);

        // Entity -> Model
        return eventMapper.toEventModel(savedEntity);
    }

    // El Puerto devuelve Optional<MODEL>
    @Override
    public Optional<EventModel> findById(Long id) {
        return jpaRepository.findById(id)
                .map(eventMapper::toEventModel);
    }

    // El Puerto devuelve Page<MODEL>
    @Override
    public Page<EventModel> findAll(Pageable pageable, String city, LocalDate date) {

        Specification<EventEntity> spec = (root, query, cb) -> cb.conjunction();

        if (city != null && !city.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("venue").get("location")), "%" + city.toLowerCase() + "%"));
        }

        if (date != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("date"), date));
        }

        // Obtiene Page<Entity>
        Page<EventEntity> entityPage = jpaRepository.findAll(spec, pageable);

        // Mapea la Page<Entity> a Page<Model>
        return entityPage.map(eventMapper::toEventModel);
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