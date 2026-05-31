package edu.uit.se122.server.social.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.common.enums.TogetherStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Together")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Together {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer togetherId;

    @Enumerated(EnumType.STRING)
    private TogetherStatus status;

    private String content;
    private Integer numOfPlayersPrefix = 0;
    private Integer numOfPlayersJoined = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Integer courtOrderId;

    @OneToMany(mappedBy = "together", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "together-member")
    private List<TogetherMember> togetherMembers;
}
