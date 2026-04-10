package edu.uit.se122.server.social.internal.repository;

import edu.uit.se122.server.social.internal.entity.Together;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TogetherRepository extends JpaRepository<Together, Integer> {
}
