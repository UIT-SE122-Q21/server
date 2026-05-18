package edu.uit.se122.server.booking.internal.repository;

import edu.uit.se122.server.booking.internal.entity.OrderInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInvoiceRepository extends JpaRepository<OrderInvoice, Integer> {
}
