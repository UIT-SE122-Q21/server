package edu.uit.se122.server.social.internal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MemberCache")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCache {
    @Id
    private Integer memberId;
    private String name;
}
