package edu.uit.se122.server.booking.internal.repository;

import edu.uit.se122.server.booking.internal.entity.CourtOrderInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtOrderInvoiceRepository extends JpaRepository<CourtOrderInvoice, Integer> {
}
