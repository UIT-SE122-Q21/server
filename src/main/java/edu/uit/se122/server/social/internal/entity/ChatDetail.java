package edu.uit.se122.server.social.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.promotion.internal.entity.Promotion;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "ChatDetail")
@Data
@ToString(exclude = "chat")
@EqualsAndHashCode(exclude = "chat")
public class ChatDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatDetailId;

    private String content;

    private LocalDateTime createdAt;

    private String senderId;

    @ManyToOne
    @JoinColumn(name = "ChatId")
    @JsonBackReference(value = "details")
    private Chat chat;
}
