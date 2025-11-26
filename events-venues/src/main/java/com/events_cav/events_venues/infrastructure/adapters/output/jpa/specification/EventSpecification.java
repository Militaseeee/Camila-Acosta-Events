package com.events_cav.events_venues.infrastructure.adapters.output.jpa.specification;

import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.EventEntity;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.VenueEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class EventSpecification {

    private EventSpecification() {}

    public static Specification<EventEntity> buildFilter(String city, LocalDate date) {

        return (root, query, criteriaBuilder) -> {

            // Usamos distinct(true) para evitar duplicados si hay múltiples joins
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            // Filtro por city, Requiere JOIN con VenueEntity
            if (city != null && !city.trim().isEmpty()) {
                // Hacemos un JOIN con la entidad VenueEntity
                Join<EventEntity, VenueEntity> venueJoin = root.join("venue");

                // Predicado: venue.city LIKE '%{city}%' (Busca sin importar mayúsculas/minúsculas)
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(venueJoin.get("city")),
                        "%" + city.toLowerCase() + "%"
                ));
            }

            // Filtro por date (Fecha exacta)
            if (date != null) {
                // Predicado: event.date = {date}
                predicates.add(criteriaBuilder.equal(root.get("date"), date));
            }

            // Optimización N+1 (join fetch)
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("venue");
            }

            // Combinamos todos los predicados con AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}