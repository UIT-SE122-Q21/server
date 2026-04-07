package edu.uit.se122.server.resource.internal.repository;

import edu.uit.se122.server.resource.internal.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtRepository extends JpaRepository<Court, Integer> {
}
