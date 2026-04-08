package edu.uit.se122.server.identity.internal.repository;

import edu.uit.se122.server.identity.internal.entity.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT MAX(m.memberId) FROM Member m")
    Integer findMaxMemberId();
    Optional<Member> findByEmail(String email);
}
