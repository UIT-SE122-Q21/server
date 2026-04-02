package edu.uit.se122.server.promotion.internal.repository;

import edu.uit.se122.server.promotion.internal.entity.PromotionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, Integer> {
}
