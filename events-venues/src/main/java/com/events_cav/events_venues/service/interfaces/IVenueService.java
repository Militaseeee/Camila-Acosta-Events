package com.events_cav.events_venues.service.interfaces;

import com.events_cav.events_venues.dto.request.VenueRequest;
import com.events_cav.events_venues.dto.response.VenueResponse;
import java.util.List;

public interface IVenueService {

    VenueResponse create(VenueRequest request);
    VenueResponse getById(Long id);
    List<VenueResponse> getAll();
    VenueResponse update(Long id, VenueRequest request);
    void delete(Long id);

}
