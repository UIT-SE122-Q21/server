package edu.uit.se122.server.social.internal.state;

import edu.uit.se122.server.common.enums.TogetherStatus;
import edu.uit.se122.server.social.internal.entity.MemberCache;
import edu.uit.se122.server.social.internal.entity.Together;
import edu.uit.se122.server.social.internal.entity.TogetherMember;
import edu.uit.se122.server.social.internal.repository.MemberCacheRepository;
import edu.uit.se122.server.social.internal.repository.TogetherMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class PendingState implements TogetherState {
    private final TogetherMemberRepository togetherMemberRepository;

    @Override
    public void joinTogether(Together together, MemberCache member) {
        if (!togetherMemberRepository.checkMemberInTogether(together.getTogetherId(), member.getMemberId())) {
            TogetherMember togetherMember = new TogetherMember();
            togetherMember.setTogether(together);
            togetherMember.setMember(member);
            together.getTogetherMembers().add(togetherMember);

            together.setNumOfPlayersJoined(together.getNumOfPlayersJoined() + 1);
            if (Objects.equals(together.getNumOfPlayersJoined(), together.getNumOfPlayersPrefix())) {
                together.setStatus(TogetherStatus.PLANNED);
                log.info("Together {} is planned", together.getTogetherId());
            } else {
                log.info("One more player joined together");
            }
        }
    }

    @Override
    public void checkOutOrder(Together together) {
        throw new IllegalStateException("Together is not planned yet");
    }

    @Override
    public void cancelTogether(Together together) {
        together.setStatus(TogetherStatus.CANCELED);
        log.info("Together {} is canceled", together.getTogetherId());
    }
}
