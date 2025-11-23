package com.events_cav.events_venues.infrastructure.adapters.output.jpa.adapter;

import com.events_cav.events_venues.domain.model.VenueModel; // 🔴 AHORA USAMOS EL MODELO
import com.events_cav.events_venues.domain.ports.output.VenueRepositoryPort; // 🔴 Implementa el Puerto de Salida
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.repository.DataVenueRepository;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.VenueEntity;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper.VenueMapper; // 🔴 Usamos el Mapper aquí
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
// El Adaptador JPA implementa el Puerto de Salida.
public class VenueJpaAdapter implements VenueRepositoryPort {

    private final DataVenueRepository jpaRepository;
    private final VenueMapper venueMapper = VenueMapper.INSTANCE; // Instanciamos el Mapper

    // El Puerto recibe MODEL y devuelve MODEL
    @Override
    public VenueModel save(VenueModel venue) {
        // Model -> Entity (Conversión interna del Adaptador)
        VenueEntity entityToSave = venueMapper.toVenueEntity(venue);

        // Guardar Entity
        VenueEntity savedEntity = jpaRepository.save(entityToSave);

        // Entity -> Model (Devolver al Use Case)
        return venueMapper.toVenueModel(savedEntity);
    }

    // El Puerto devuelve Optional<MODEL>
    @Override
    public Optional<VenueModel> findById(Long id) {
        // Mapea la Entity a Model si existe antes de devolver
        return jpaRepository.findById(id).map(venueMapper::toVenueModel);
    }

    // El Puerto devuelve List<MODEL>
    @Override
    public List<VenueModel> findAll() {
        // Obtiene List<Entity> y la mapea a List<Model>
        return jpaRepository.findAll().stream()
                .map(venueMapper::toVenueModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    // Estos métodos ya estaban bien
    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        return jpaRepository.existsByNameAndIdNot(name, id);
    }
}