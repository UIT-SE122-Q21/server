package edu.uit.se122.server.social.internal.state;

import edu.uit.se122.server.common.enums.TogetherStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TogetherStateFactory {
    private final TogetherState pendingState;
    private final TogetherState plannedState;

    public TogetherState getState(TogetherStatus status) {
        return switch (status) {
            case PENDING -> pendingState;
            case PLANNED -> plannedState;
            default -> throw new IllegalArgumentException("Invalid status");
        };
    }
}
