package edu.uit.se122.server.identity.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.common.enums.TokenType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "MemberToken")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MemberToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberTokenId;

    private String token;

    private TokenType type;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    @JsonBackReference(value = "memberTokens")
    private Member member;

    public MemberToken(String token, Member member) {
        this.token = token;
        this.member = member;
        // Set thời gian hết hạn của token là 15 phút kể từ lúc tạo
        this.expiredAt = LocalDateTime.now().plusMinutes(15);
    }
}
