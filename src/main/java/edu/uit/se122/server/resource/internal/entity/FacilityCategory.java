package edu.uit.se122.server.resource.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "FacilityCategory")
@Data
@ToString(exclude = {"facilities", "criteria", "maintains", "brokenReports"})
@EqualsAndHashCode(exclude = {"facilities", "criteria", "maintains", "brokenReports"})
public class FacilityCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer facilityCategoryId;

    private String name;

    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "facilities")
    private List<Facility> facilities;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "criteria")
    private List<FacilityCriterion> criteria;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonBackReference(value = "maintains_category")
    private List<Maintain> maintains;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonBackReference(value = "reports_category")
    private List<BrokenReport> brokenReports;
}
