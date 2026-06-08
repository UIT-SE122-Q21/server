package edu.uit.se122.server.social.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.common.enums.MessageType;
import edu.uit.se122.server.promotion.internal.entity.Promotion;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_detail")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ChatDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatDetailId;

    @Indexed
    private Integer togetherId;

    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(updatable = false)
    private Integer memberId;

    private MessageType type;
}
