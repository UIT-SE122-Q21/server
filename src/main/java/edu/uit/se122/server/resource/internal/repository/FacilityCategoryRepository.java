package edu.uit.se122.server.resource.internal.repository;

import edu.uit.se122.server.resource.internal.entity.FacilityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityCategoryRepository extends JpaRepository<FacilityCategory, Integer> {
}
