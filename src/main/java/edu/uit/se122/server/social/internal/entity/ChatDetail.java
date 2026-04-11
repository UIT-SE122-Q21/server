package edu.uit.se122.server.social.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.promotion.internal.entity.Promotion;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "ChatDetail")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = "chat")
@EqualsAndHashCode(exclude = "chat")
public class ChatDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatDetailId;

    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(updatable = false)
    private String memberId;

    @ManyToOne
    @JoinColumn(name = "ChatId")
    @JsonBackReference(value = "details")
    private Chat chat;
}
