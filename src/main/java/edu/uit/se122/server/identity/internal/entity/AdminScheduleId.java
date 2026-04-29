package edu.uit.se122.server.identity.internal.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class AdminScheduleId implements Serializable {

    private Integer adminId;

    private Integer scheduleId;
}
