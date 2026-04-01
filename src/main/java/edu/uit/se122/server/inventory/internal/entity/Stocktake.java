package edu.uit.se122.server.inventory.internal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Stocktake")
@Data
public class Stocktake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stocktakeId;

    private String content;

    private LocalDateTime createdAt;
}
