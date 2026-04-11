package edu.uit.se122.server.identity.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Member")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    private Integer memberId;

    private String name;

    private String email;

    private String password;

    private Boolean verified;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "memberTokens")
    private List<MemberToken> memberTokens;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "refreshToken")
    private RefreshToken refreshToken;
}
