package edu.uit.se122.server.social.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.common.enums.TogetherStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Together")
@Data
@ToString(exclude = "chat")
@EqualsAndHashCode(exclude = "chat")
public class Together {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer togetherId;

    @Enumerated(EnumType.STRING)
    private TogetherStatus status;

    private String content;

    private Integer numOfPlayers;

    private LocalDate planDate;

    private LocalDateTime fromTime;

    private LocalDateTime toTime;

    private String memberId;

    private Integer courtOrderId;

    @OneToOne(mappedBy = "together", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "chat")
    private Chat chat;
}
