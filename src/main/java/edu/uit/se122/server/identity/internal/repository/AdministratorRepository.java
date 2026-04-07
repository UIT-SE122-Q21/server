package edu.uit.se122.server.identity.internal.repository;

import edu.uit.se122.server.identity.internal.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
}
