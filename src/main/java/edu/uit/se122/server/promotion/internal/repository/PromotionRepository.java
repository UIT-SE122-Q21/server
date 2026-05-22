package edu.uit.se122.server.promotion.internal.repository;

import edu.uit.se122.server.promotion.internal.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    @Query("SELECT p FROM Promotion p WHERE p.hidden = false AND CURRENT_DATE BETWEEN p.startDate AND p.endDate")
    List<Promotion> findActivePromotions();
}
