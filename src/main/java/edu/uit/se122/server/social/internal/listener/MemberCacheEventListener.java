package edu.uit.se122.server.social.internal.listener;

import edu.uit.se122.server.identity.AuthContract;
import edu.uit.se122.server.social.internal.entity.MemberCache;
import edu.uit.se122.server.social.internal.repository.MemberCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberCacheEventListener {
    private final MemberCacheRepository  memberCacheRepository;

    @ApplicationModuleListener
    void onMemberCreated(AuthContract.CreatedMemberEvent event) {
        MemberCache  memberCache = new MemberCache(event.memberId(), event.name());
        memberCacheRepository.save(memberCache);
    }
}
