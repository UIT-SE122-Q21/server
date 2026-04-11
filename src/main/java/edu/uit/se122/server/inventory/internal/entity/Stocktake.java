package edu.uit.se122.server.inventory.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Stocktake")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = "stocktakeDetails")
@EqualsAndHashCode(exclude = "stocktakeDetails")
public class Stocktake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stocktakeId;

    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "stocktake", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "stocktakeDetails")
    private List<StocktakeDetail> stocktakeDetails;
}
