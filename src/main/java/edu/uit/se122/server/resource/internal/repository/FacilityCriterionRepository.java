package edu.uit.se122.server.resource.internal.repository;

import edu.uit.se122.server.resource.internal.entity.FacilityCriterion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityCriterionRepository extends JpaRepository<FacilityCriterion, Integer> {
}
