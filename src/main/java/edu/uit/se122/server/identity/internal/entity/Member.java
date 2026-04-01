package edu.uit.se122.server.identity.internal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Member")
@Data
public class Member {
    @Id
    private String memberId;

    private String memberName;

    private String email;

    private String passwordHash;

    private LocalDateTime createdAt;
}
