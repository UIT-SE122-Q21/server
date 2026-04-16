package edu.uit.se122.server.booking.internal.repository;

import edu.uit.se122.server.booking.internal.entity.ProductCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCacheRepository extends JpaRepository<ProductCache, Integer> {
}
