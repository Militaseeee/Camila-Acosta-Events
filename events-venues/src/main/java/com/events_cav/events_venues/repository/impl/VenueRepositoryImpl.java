package com.events_cav.events_venues.repository.impl;

import com.events_cav.events_venues.entity.VenueEntity;
import com.events_cav.events_venues.repository.interfaces.DataVenueRepository;
import com.events_cav.events_venues.repository.interfaces.IVenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor // Lombok inyecta el dataVenueRepository automáticamente
public class VenueRepositoryImpl implements IVenueRepository {

    private final DataVenueRepository jpaRepository; // Inyectamos la interfaz de JPA

    @Override
    public VenueEntity save(VenueEntity venue) {
        return jpaRepository.save(venue);
    }

    @Override
    public Optional<VenueEntity> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<VenueEntity> findAll() {
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