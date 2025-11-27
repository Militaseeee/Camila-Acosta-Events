package com.events_cav.events_venues.infrastructure.adapters.output.jpa.adapter;

import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.output.VenueRepositoryPort;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.repository.DataVenueRepository;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.VenueEntity;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper.VenueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor // Se encarga de inyectar los campos final
public class VenueJpaAdapter implements VenueRepositoryPort {

    private final DataVenueRepository jpaRepository;
    private final VenueMapper venueMapper;

    // El Puerto recibe MODEL y devuelve MODEL
    @Override
    public VenueModel save(VenueModel venue) {
        // Model -> Entity (Conversión interna del Adaptador)
        VenueEntity entityToSave = venueMapper.toVenueEntity(venue); // Usa el mapper inyectado

        // Guardar Entity
        VenueEntity savedEntity = jpaRepository.save(entityToSave);

        // Entity -> Model (Devolver al Use Case)
        return venueMapper.toVenueModel(savedEntity); // Usa el mapper inyectado
    }

    // El Puerto devuelve Optional<MODEL>
    @Override
    public Optional<VenueModel> findById(Long id) {
        // Mapea la Entity a Model si existe antes de devolver
        return jpaRepository.findById(id).map(venueMapper::toVenueModel);
    }

    @Override
    public Page<VenueModel> findAll(Pageable pageable) {
        // Llama al metodo paginado del repositorio JPA, obteniendo Page<VenueEntity>
        Page<VenueEntity> entityPage = jpaRepository.findAll(pageable);

        // Mapea Page<VenueEntity> a Page<VenueModel>
        return entityPage.map(venueMapper::toVenueModel);
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