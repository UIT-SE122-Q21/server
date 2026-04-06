package edu.uit.se122.server.resource.internal.repository;

import edu.uit.se122.server.resource.internal.entity.Facility;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT MAX(f.facilityId) FROM Facility f WHERE f.category.facilityCategoryId = :categoryId")
    Integer findMaxProductIdByCategoryId(@Param("categoryId") Integer categoryId);
}
