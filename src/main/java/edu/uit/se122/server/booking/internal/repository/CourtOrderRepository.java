package edu.uit.se122.server.booking.internal.repository;

import edu.uit.se122.server.booking.OrderContract;
import edu.uit.se122.server.booking.internal.entity.CourtOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
        WHERE co.status = "Ordered"
        AND pod.productId = :productId
        AND co.orderDate >= :orderDate
    """)
    Integer countOrderedProduct(
            @Param("productId") Integer productId,
            @Param("orderDate") LocalDate orderDate
    );

    @Query("""
       SELECT cod.courtId as courtId, co.courtOrderId as courtOrderId, co.orderDate as orderDate, co.startHour as startHour, co.endHour as endHour
       FROM CourtOrder co
       JOIN co.courtOrderDetails cod
       WHERE co.orderDate = :orderDate
       AND (co.status = 'WaitingForPayment' OR co.status = 'Ordered')
    """)
    List<OrderContract.CourtRes> getAllCourtSchedule(@Param("orderDate") LocalDate orderDate);

    List<CourtOrder> findByMemberId(Integer memberId);
}
