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
public class FacilityCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer facilityCategoryId;

    private String name;

    private String description;
}
