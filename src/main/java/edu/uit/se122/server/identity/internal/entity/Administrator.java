package edu.uit.se122.server.identity.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.common.enums.AdminRole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Administrator")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Administrator {
    @Id
    private Integer adminId;

    @Column(nullable = false)
    private String name;

    private String email;

    private String phoneNumber;

    private String password;

    private String color;

    @Enumerated(EnumType.STRING)
    private AdminRole role;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "admin-adminSchedules")
    private List<AdminSchedule> adminSchedules;
}
