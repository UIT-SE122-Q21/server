package edu.uit.se122.server.identity.internal.entity;

import edu.uit.se122.server.common.enums.AdminRole;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Administrator")
@Data
public class Administrator {
    @Id
    private String adminId;

    @Column(nullable = false)
    private String adminName;

    @Column(nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private AdminRole role;

    // Navigation: Một Admin có thể lập nhiều lịch làm việc
    @OneToMany(mappedBy = "admin")
    private List<EmployeeSchedule> schedules;
}
