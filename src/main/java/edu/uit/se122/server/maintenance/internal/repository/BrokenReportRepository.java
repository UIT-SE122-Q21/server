package edu.uit.se122.server.maintenance.internal.repository;

import edu.uit.se122.server.maintenance.internal.entity.BrokenReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokenReportRepository extends JpaRepository<BrokenReport, Integer> {
}
