package edu.uit.se122.server.booking.internal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CourtCache")
@Data
public class CourtCache {
    @Id
    private Integer courtId;
    private String name;
    private Double unitPrice;
}
