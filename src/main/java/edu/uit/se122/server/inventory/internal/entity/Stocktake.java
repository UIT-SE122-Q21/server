package edu.uit.se122.server.inventory.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Stocktake")
@Data
@ToString(exclude = "stocktakeDetails")
@EqualsAndHashCode(exclude = "stocktakeDetails")
public class Stocktake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stocktakeId;

    private String content;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "stocktake", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "stocktakeDetails")
    private List<StocktakeDetail> stocktakeDetails;
}
