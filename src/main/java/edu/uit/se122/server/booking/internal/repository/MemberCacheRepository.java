package edu.uit.se122.server.booking.internal.repository;

import edu.uit.se122.server.booking.internal.entity.MemberCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberCacheRepository extends JpaRepository<MemberCache, Integer> {
}
