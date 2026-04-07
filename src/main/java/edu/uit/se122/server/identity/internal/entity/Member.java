package edu.uit.se122.server.identity.internal.entity;

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

    private LocalDateTime createdAt;
}
