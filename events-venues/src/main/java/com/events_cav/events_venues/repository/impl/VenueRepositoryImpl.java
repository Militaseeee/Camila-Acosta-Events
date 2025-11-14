package com.events_cav.events_venues.repository.impl;

import com.events_cav.events_venues.model.Venue;
import com.events_cav.events_venues.repository.interfaces.DataVenueRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class VenueRepositoryImpl implements DataVenueRepository {

    // Acá guardo los datos en un array
    private final List<Venue> venues = new ArrayList<>();

    // Genera id en orden
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Venue save(Venue venue) {

        Long newId = idGenerator.getAndIncrement(); // Se debe generar el ID nuevo
        Venue newVenue = new Venue(newId, venue.name(), venue.location()); // Acá creamos un objeto nuevo asignado por el ID

        venues.add(newVenue);
        return newVenue;
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return venues.stream()
                .filter(v -> v.id_venue().equals(id))
                .findFirst();
    }

    @Override
    public List<Venue> findAll() {
        return new ArrayList<>(venues);
    }

    @Override
    public void update(Long id, Venue venueUpdate) {
        deleteById(id); // Borramos el viejo
        venues.add(venueUpdate); // Agregamos el nuevo (que ya debe traer el ID)
    }

    @Override
    public void deleteById(Long id) {
        venues.removeIf(v -> v.id_venue().equals(id));
    }
}
