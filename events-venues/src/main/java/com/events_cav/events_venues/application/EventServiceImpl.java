package com.events_cav.events_venues.application;

import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.EventRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.EventResponse;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.EventEntity;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.VenueEntity;
import com.events_cav.events_venues.domain.exception.BadRequestException;
import com.events_cav.events_venues.domain.exception.ResourceConflictException;
import com.events_cav.events_venues.domain.exception.ResourceNotFoundException;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper.EventMapper;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper.VenueMapper; // Necesario para mapear VenueEntity a VenueModel
import com.events_cav.events_venues.domain.model.EventModel;
import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.output.IEventRepository;
import com.events_cav.events_venues.domain.ports.output.IVenueRepository;
import com.events_cav.events_venues.domain.ports.input.IEventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class EventServiceImpl implements IEventService {

    private final IEventRepository eventRepository;
    private final IVenueRepository venueRepository;
    // Asumiendo que VenueMapper está disponible para usarlo aquí
    private final VenueMapper venueMapper = VenueMapper.INSTANCE;

    public EventServiceImpl(IEventRepository eventRepository, IVenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public EventResponse create(EventRequest request) {
        if (eventRepository.existsByName(request.getName())) {
            throw new BadRequestException("An event with name '" + request.getName() + "' already exists");
        }

        // Buscar la VenueEntity (persistencia)
        VenueEntity venueEntity = venueRepository.findById(request.getIdVenue())
                .orElseThrow(() -> new ResourceNotFoundException("The Venue with ID " + request.getIdVenue() + " does not exist"));

        // Convertir VenueEntity -> VenueModel (dominio)
        VenueModel venueModel = venueMapper.toVenueModel(venueEntity);

        // DTO Request -> Model (dominio)
        EventModel eventModel = EventMapper.INSTANCE.toEventModel(request);

        // Asigna el objeto Model puro (lógica de negocio)
        eventModel.setVenue(venueModel);

        // Model -> Entity (Para guardar en BD)
        EventEntity entityToSave = EventMapper.INSTANCE.toEventEntity(eventModel);

        // MapStruct, al mapear EventModel -> EventEntity, no puede mapear VenueModel -> VenueEntity
        // porque el EventMapper solo conoce la relación de los IDs
        entityToSave.setVenue(venueEntity);

        EventEntity savedEntity = eventRepository.save(entityToSave);

        // 6. Entity -> Model (Recuperar ID generado)
        EventModel savedModel = EventMapper.INSTANCE.toEventModel(savedEntity);

        // 7. Model -> DTO Response
        return EventMapper.INSTANCE.toEventResponse(savedModel);
    }

    @Override
    public EventResponse getById(Long id) {
        // Obtener la Entity
        EventEntity entity = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        // Convertir Entity -> Model
        EventModel model = EventMapper.INSTANCE.toEventModel(entity);

        // Convertir Model -> Response
        return EventMapper.INSTANCE.toEventResponse(model);
    }

    // Metodo paginado y con filtros
    @Override
    public Page<EventResponse> getAll(Pageable pageable, String city, LocalDate date) {
        // Obtener la página paginada y filtrada de ENTIDADES
        Page<EventEntity> eventsPage = eventRepository.findAll(pageable, city, date);

        // Mapear Page<EventEntity> a Page<EventModel>
        Page<EventModel> modelsPage = eventsPage.map(EventMapper.INSTANCE::toEventModel);

        // Mapear Page<EventModel> a Page<EventResponse>
        return modelsPage.map(EventMapper.INSTANCE::toEventResponse);
    }

    @Override
    public EventResponse update(Long id, EventRequest request) {
        // Buscar Evento (Entity)
        EventEntity currentEntity = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        // Validar nombre duplicado
        if (eventRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new ResourceConflictException("An event with name '" + request.getName() + "' already exists");
        }

        // Buscar el nuevo Venue (Entity)
        VenueEntity newVenueEntity = venueRepository.findById(request.getIdVenue())
                .orElseThrow(() -> new ResourceNotFoundException("The Venue destination does not exist"));

        // Convertir a Model para aplicar la lógica de negocio
        EventModel currentModel = EventMapper.INSTANCE.toEventModel(currentEntity);
        VenueModel newVenueModel = venueMapper.toVenueModel(newVenueEntity);

        // Actualizar Model (Lógica de Negocio)
        currentModel.setName(request.getName());
        currentModel.setDate(request.getDate());
        currentModel.setVenue(newVenueModel);

        // Model -> Entity (Para guardar/update)
        EventEntity entityToUpdate = EventMapper.INSTANCE.toEventEntity(currentModel);

        // Asigna la VenueEntity real -> esta parte es importante
        entityToUpdate.setVenue(newVenueEntity);

        // Asegurarse de que el ID del evento se conserve para que JPA haga UPDATE y no INSERT
        entityToUpdate.setId(id);

        EventEntity updatedEntity = eventRepository.save(entityToUpdate);

        // 6. Entity -> Model -> Response
        EventModel updatedModel = EventMapper.INSTANCE.toEventModel(updatedEntity);
        return EventMapper.INSTANCE.toEventResponse(updatedModel);
    }

    @Override
    public void delete(Long id) {
        if (eventRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Event not found with ID: " + id);
        }
        eventRepository.deleteById(id);
    }
}