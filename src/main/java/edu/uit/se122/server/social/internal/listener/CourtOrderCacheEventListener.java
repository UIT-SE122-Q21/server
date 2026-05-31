package edu.uit.se122.server.social.internal.listener;

import edu.uit.se122.server.booking.OrderContract;
import edu.uit.se122.server.social.internal.entity.CourtOrderCache;
import edu.uit.se122.server.social.internal.repository.CourtOrderCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourtOrderCacheEventListener {
    private final CourtOrderCacheRepository courtOrderCacheRepository;

    @ApplicationModuleListener
    void onOrderCreated(OrderContract.CreatedOrderEvent event) {
        CourtOrderCache courtOrderCache = CourtOrderCache.builder()
                .courtOrderId(event.courtOrderId())
                .orderDate(event.orderDate())
                .startHour(event.startHour())
                .endHour(event.endHour())
                .build();
        courtOrderCacheRepository.save(courtOrderCache);
    }
}
