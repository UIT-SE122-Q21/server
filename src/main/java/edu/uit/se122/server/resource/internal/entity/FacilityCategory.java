package edu.uit.se122.server.resource.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "FacilityCategory")
@Data
@ToString(exclude = {"facilities", "criteria"})
@EqualsAndHashCode(exclude = {"facilities", "criteria"})
public class FacilityCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer facilityCategoryId;

    private String facilityCategoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "facilities")
    private List<Facility> facilities;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "criteria")
    private List<FacilityCriterion> criteria;
}
