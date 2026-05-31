package edu.uit.se122.server.social.internal.entity;

import edu.uit.se122.server.common.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "CourtOrderCache")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourtOrderCache {
    @Id
    private Integer courtOrderId;
    private LocalDate orderDate;
    private LocalTime startHour;
    private LocalTime endHour;
}
