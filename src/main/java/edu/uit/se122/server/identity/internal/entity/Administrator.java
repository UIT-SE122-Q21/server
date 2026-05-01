package edu.uit.se122.server.identity.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.common.enums.AdminRole;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Administrator")
@Data
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

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "admin-adminSchedules")
    private List<AdminSchedule> adminSchedules;
}
