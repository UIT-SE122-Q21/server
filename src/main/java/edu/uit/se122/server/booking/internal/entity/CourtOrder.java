package edu.uit.se122.server.booking.internal.entity;

import edu.uit.se122.server.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "CourtOrder")
@Data
public class CourtOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courtOrderId;

    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String adminId;

    private String userId;

    private Boolean isGuest;

    // Navigation: Chi tiết các sân được đặt trong đơn này
    @OneToMany(mappedBy = "courtOrder", cascade = CascadeType.ALL)
    private List<CourtOrderDetail> details;
}
