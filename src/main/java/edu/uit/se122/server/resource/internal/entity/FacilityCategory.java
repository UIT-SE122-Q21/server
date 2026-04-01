package edu.uit.se122.server.resource.internal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "FacilityCategory")
@Data
public class FacilityCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer facilityCategoryId;

    private String facilityCategoryName;

    @OneToMany(mappedBy = "category")
    private List<Facility> facilities;
}
