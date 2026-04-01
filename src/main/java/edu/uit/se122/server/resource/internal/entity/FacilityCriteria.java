package edu.uit.se122.server.resource.internal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "FacilityCriteria")
@Data
public class FacilityCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer criteriaId;

    private String detail;

    private LocalDateTime schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FacilityCategoryId")
    private FacilityCategory category;
}
