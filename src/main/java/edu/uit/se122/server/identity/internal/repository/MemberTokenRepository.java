package edu.uit.se122.server.identity.internal.repository;

import edu.uit.se122.server.identity.internal.entity.MemberToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberTokenRepository extends JpaRepository<MemberToken, Integer> {
    Optional<MemberToken> findByToken(String token);
}
