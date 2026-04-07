package edu.uit.se122.server.identity.internal.repository;

import edu.uit.se122.server.identity.internal.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByEmail(String email);
}
