package edu.uit.se122.server.identity.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "AdminSchedule")
@Data
public class AdminSchedule {

    @EmbeddedId
    private AdminScheduleId id = new AdminScheduleId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("adminId")
    @JoinColumn(name = "admin_id")
    @JsonBackReference(value = "admin-adminSchedules")
    private Administrator admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("scheduleId")
    @JoinColumn(name = "schedule_id")
    @JsonBackReference(value = "schedule-adminSchedules")
    private Schedule schedule;
}
