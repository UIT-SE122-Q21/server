package edu.uit.se122.server.social.internal.repository;

import edu.uit.se122.server.social.internal.entity.CourtOrderCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtOrderCacheRepository extends JpaRepository<CourtOrderCache, Integer> {
}
