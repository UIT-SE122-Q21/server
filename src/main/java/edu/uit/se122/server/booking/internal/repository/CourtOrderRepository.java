package edu.uit.se122.server.booking.internal.repository;

import edu.uit.se122.server.booking.internal.entity.CourtOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface CourtOrderRepository extends JpaRepository<CourtOrder, Integer> {
    @Query("""
        SELECT COALESCE(SUM(pod.quantity), 0)
        FROM CourtOrder co
        JOIN co.productOrderDetails pod
        WHERE co.status = "Ordered"
        AND pod.productId = :productId
        AND co.orderDate = :orderDate
        AND
        ((:startHour >= co.startHour AND :startHour < co.endHour)
        OR
        (:endHour > co.startHour AND :endHour <= co.endHour))
    """)
    Integer countUnavailableRacket(
            @Param("productId") Integer productId,
            @Param("orderDate") LocalDate orderDate,
            @Param("startHour") LocalTime startHour,
            @Param("endHour") LocalTime endHour
    );

    @Query("""
        SELECT COALESCE(SUM(pod.quantity), 0)
        FROM CourtOrder co
        JOIN co.productOrderDetails pod
        WHERE pod.draft = true
        AND pod.productId = :productId
        AND co.orderDate >= :orderDate
    """)
    Integer countOrderedProduct(
            @Param("productId") Integer productId,
            @Param("orderDate") LocalDate orderDate
    );
}
