package edu.uit.se122.server.identity.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Member")
@Data
public class Member {
    @Id
    private Integer memberId;

    private String name;

    private String email;

    private String passwordHash;

    private Boolean verified;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "memberTokens")
    private List<MemberToken> memberTokens;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "refreshToken")
    private RefreshToken refreshToken;
}
