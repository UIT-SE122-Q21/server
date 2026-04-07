package edu.uit.se122.server.identity.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.common.enums.AdminRole;
import edu.uit.se122.server.inventory.internal.entity.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Administrator")
@Data
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    @Column(nullable = false)
    private String name;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private AdminRole role;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "employeeSchedules")
    private List<EmployeeSchedule> employeeSchedules;
}
