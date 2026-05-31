package edu.uit.se122.server.social.internal.repository;

import edu.uit.se122.server.social.internal.entity.MemberCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberCacheRepository extends JpaRepository<MemberCache, Integer> {
}
