package edu.uit.se122.server.resource.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "FacilityCriterion")
@Data
@ToString(exclude = "category")
@EqualsAndHashCode(exclude = "category")
public class FacilityCriterion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer criterionId;

    private String detail;

    private LocalDateTime schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FacilityCategoryId")
    private FacilityCategory category;
}
