package edu.uit.se122.server.social.internal.listener;

import edu.uit.se122.server.booking.OrderContract;
import edu.uit.se122.server.social.internal.entity.Together;
import edu.uit.se122.server.social.internal.repository.TogetherRepository;
import edu.uit.se122.server.social.internal.state.TogetherState;
import edu.uit.se122.server.social.internal.state.TogetherStateFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckOutEventListener {
    private final TogetherRepository togetherRepository;
    private final TogetherStateFactory stateFactory;

    @ApplicationModuleListener
    public void handleCheckOutEvent(OrderContract.CheckOutEvent event) {
        Together together = togetherRepository.findById(event.courtOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Together not found for court order ID: " + event.courtOrderId()));
        TogetherState currentState = stateFactory.getState(together.getStatus());
        currentState.checkOutOrder(together);
        togetherRepository.save(together);
    }
}
