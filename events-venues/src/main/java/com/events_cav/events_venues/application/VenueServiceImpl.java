package com.events_cav.events_venues.application;

import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.VenueRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.VenueResponse;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.VenueEntity;
import com.events_cav.events_venues.domain.exception.BadRequestException;
import com.events_cav.events_venues.domain.exception.ResourceConflictException;
import com.events_cav.events_venues.domain.exception.ResourceNotFoundException;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper.VenueMapper;
import com.events_cav.events_venues.domain.model.VenueModel;
import com.events_cav.events_venues.domain.ports.output.IVenueRepository;
import com.events_cav.events_venues.domain.ports.input.IVenueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VenueServiceImpl implements IVenueService {

    private final IVenueRepository venueRepository;

    public VenueServiceImpl(IVenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public VenueResponse create(VenueRequest request) {
        // La validación sigue usando el Repositorio, que trabaja con el nombre (String)
        if (venueRepository.existsByName(request.getName())) {
            throw new ResourceConflictException("A venue with name '" + request.getName() + "' already exists");
        }

        // DTO Request -> MODEL (Objeto de Negocio)
        VenueModel model = VenueMapper.INSTANCE.toVenueModel(request);

        // MODEL -> ENTITY (Para guardar)
        VenueEntity entityToSave = VenueMapper.INSTANCE.toVenueEntity(model);
        VenueEntity savedEntity = venueRepository.save(entityToSave);

        // ENTITY -> MODEL (Actualizar ID)
        VenueModel savedModel = VenueMapper.INSTANCE.toVenueModel(savedEntity);

        // MODEL -> DTO Response (Para devolver)
        return VenueMapper.INSTANCE.toVenueResponse(savedModel);
    }

    @Override
    public VenueResponse getById(Long id) {
        // Obtener la Entity
        VenueEntity entity = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + id));

        // Convertir Entity -> Model
        VenueModel model = VenueMapper.INSTANCE.toVenueModel(entity);

        // Convertir Model -> Response
        return VenueMapper.INSTANCE.toVenueResponse(model);
    }

    @Override
    public List<VenueResponse> getAll() {
        // Obtener List<Entity>
        List<VenueEntity> entities = venueRepository.findAll();

        // Mapear List<Entity> -> List<Model> -> List<Response>
        return entities.stream()
                .map(VenueMapper.INSTANCE::toVenueModel)
                .map(VenueMapper.INSTANCE::toVenueResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VenueResponse update(Long id, VenueRequest request) {
        // Buscar existente (Entity)
        VenueEntity currentEntity = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + id));

        // Validar nombre duplicado
        if (venueRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BadRequestException("A venue with name '" + request.getName() + "' already exists");
        }

        // Convertir a Model para trabajar con él (y conservar el ID)
        VenueModel currentModel = VenueMapper.INSTANCE.toVenueModel(currentEntity);

        // Actualizar campos del Model con los datos del Request
        currentModel.setName(request.getName());
        currentModel.setLocation(request.getLocation());

        // Convertir Model -> Entity (Para guardar/update)
        VenueEntity entityToUpdate = VenueMapper.INSTANCE.toVenueEntity(currentModel);

        // Guardar cambios
        VenueEntity updatedEntity = venueRepository.save(entityToUpdate);

        // Convertir Entity -> Model -> Response
        VenueModel updatedModel = VenueMapper.INSTANCE.toVenueModel(updatedEntity);
        return VenueMapper.INSTANCE.toVenueResponse(updatedModel);
    }

    @Override
    public void delete(Long id) {
        if (venueRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Cannot delete. Venue not found with ID: " + id);
        }
        venueRepository.deleteById(id);
    }
}