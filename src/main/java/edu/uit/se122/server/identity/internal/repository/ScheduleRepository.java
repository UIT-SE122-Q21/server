package edu.uit.se122.server.identity.internal.repository;

import edu.uit.se122.server.identity.internal.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
}
