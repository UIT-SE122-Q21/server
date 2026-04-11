package edu.uit.se122.server.identity.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.booking.internal.entity.CourtOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "EmployeeSchedule")
@Data
public class EmployeeSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeScheduleId;

    private LocalDate workDate;

    private LocalTime fromTime;

    private LocalTime toTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId")
    @JsonBackReference(value = "employeeSchedules")
    private Administrator admin;
}
