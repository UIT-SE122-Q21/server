package edu.uit.se122.server.booking.internal.component;

import edu.uit.se122.server.booking.internal.entity.MemberCache;
import edu.uit.se122.server.booking.internal.repository.MemberCacheRepository;
import edu.uit.se122.server.identity.MemberContract;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberCacheEventListener {
    private final MemberCacheRepository cacheRepository;

    @ApplicationModuleListener
    void onMemberVerified(MemberContract.VerifiedEvent dto) {
        MemberCache memberCache = new MemberCache();
        memberCache.setMemberId(dto.memberId());
        memberCache.setName(dto.name());
        cacheRepository.save(memberCache);
    }
}
