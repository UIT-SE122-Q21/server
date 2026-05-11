package edu.uit.se122.server.booking.internal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "member_cache")
@Data
public class MemberCache {
    @Id
    private Integer memberId;
    private String name;
}
