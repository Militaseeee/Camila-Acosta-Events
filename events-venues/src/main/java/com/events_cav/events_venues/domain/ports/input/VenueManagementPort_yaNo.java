package com.events_cav.events_venues.domain.ports.input;

import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.VenueRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response.VenueResponse;
import java.util.List;

public interface VenueManagementPort_yaNo {

    VenueResponse create(VenueRequest request);
    VenueResponse getById(Long id);
    List<VenueResponse> getAll();
    VenueResponse update(Long id, VenueRequest request);
    void delete(Long id);

}
