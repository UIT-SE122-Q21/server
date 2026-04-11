package edu.uit.se122.server.social.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Chat")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"together", "details"})
@EqualsAndHashCode(exclude = {"together", "details"})
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "togetherId")
    @JsonBackReference(value = "chat")
    private Together together;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "details")
    private List<ChatDetail> details;
}
