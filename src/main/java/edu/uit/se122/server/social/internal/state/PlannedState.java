package edu.uit.se122.server.social.internal.state;

import edu.uit.se122.server.common.enums.TogetherStatus;
import edu.uit.se122.server.social.internal.entity.MemberCache;
import edu.uit.se122.server.social.internal.entity.Together;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PlannedState implements TogetherState {

    @Override
    public void joinTogether(Together together, MemberCache member) {
        throw new IllegalStateException("Together is already planned! Please wait for another");
    }

    @Override
    public void checkOutOrder(Together together) {
        together.setStatus(TogetherStatus.COMPLETED);
        log.info("Together {} is completed", together.getTogetherId());
    }

    @Override
    public void cancelTogether(Together together) {
        throw new IllegalStateException("Together is already planned! It cannot be canceled");
    }
}
