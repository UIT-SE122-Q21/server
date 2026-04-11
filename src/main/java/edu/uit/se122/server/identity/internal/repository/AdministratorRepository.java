package edu.uit.se122.server.identity.internal.repository;

import edu.uit.se122.server.identity.internal.entity.Administrator;
import edu.uit.se122.server.identity.internal.entity.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT MAX(a.adminId) FROM Administrator a")
    Integer findMaxAdminId();
}
