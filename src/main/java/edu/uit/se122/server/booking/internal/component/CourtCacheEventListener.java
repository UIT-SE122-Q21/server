package edu.uit.se122.server.booking.internal.component;

import edu.uit.se122.server.booking.internal.entity.CourtCache;
import edu.uit.se122.server.booking.internal.repository.CourtCacheRepository;
import edu.uit.se122.server.resource.CourtContract;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourtCacheEventListener {
    private final CourtCacheRepository cacheRepository;

    @ApplicationModuleListener
    void onCourtUpdated(CourtContract.CreatedEvent event) {
        CourtCache courtCache = new CourtCache();
        courtCache.setCourtId(event.courtId());
        courtCache.setName(event.name());
        courtCache.setUnitPrice(event.unitPrice());
        cacheRepository.save(courtCache);
    }
}
