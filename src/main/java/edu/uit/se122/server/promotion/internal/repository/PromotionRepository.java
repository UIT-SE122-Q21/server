package edu.uit.se122.server.promotion.internal.repository;

import edu.uit.se122.server.promotion.internal.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
}
