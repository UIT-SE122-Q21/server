package edu.uit.se122.server.social.internal.entity;

import edu.uit.se122.server.promotion.internal.entity.Promotion;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "ChatDetail")
@Data
public class ChatDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatDetailId;

    private String content;

    private LocalDateTime createdAt;

    private String senderId;

    @ManyToOne
    @JoinColumn(name = "ChatId")
    private Chat chat;
}
