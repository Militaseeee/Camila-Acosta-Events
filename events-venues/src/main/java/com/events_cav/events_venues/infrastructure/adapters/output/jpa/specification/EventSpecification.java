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

    // La firma es correcta: recibe city, dateStart y dateEnd
    public static Specification<EventEntity> buildFilter(String city, LocalDate dateStart, LocalDate dateEnd) {

        return (root, query, criteriaBuilder) -> {

            // Usamos distinct(true) para evitar duplicados si hay múltiples joins
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            // Filtro por city (No requiere cambios)
            if (city != null && !city.trim().isEmpty()) {
                // Hacemos un JOIN con la entidad VenueEntity
                Join<EventEntity, VenueEntity> venueJoin = root.join("venue");

                // Predicado: venue.city LIKE '%{city}%'
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(venueJoin.get("city")),
                        "%" + city.toLowerCase() + "%"
                ));
            }

            // Si se proporciona dateStart, la fecha de inicio del evento (EventEntity.startDate)
            // debe ser mayor o igual a la fecha de inicio del rango solicitado
            if (dateStart != null) {
                // Predicado: event.startDate >= dateStart
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), dateStart));
            }

            // Si se proporciona dateEnd, la fecha de inicio del evento (EventEntity.startDate)
            // debe ser menor o igual a la fecha de fin del rango solicitado.
            if (dateEnd != null) {
                // Predicado: event.startDate <= dateEnd
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), dateEnd));
            }

            // Esto filtra por startDate entre dateStart y dateEnd
            // Si el filtro debe considerar rangos (startDate–endDate), la lógica cambia
            // Optimización N+1 (join fetch)
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("venue");
            }

            // Combinamos todos los predicados con AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}