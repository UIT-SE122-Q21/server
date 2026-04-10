package edu.uit.se122.server.identity.internal.repository;

import edu.uit.se122.server.identity.internal.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
//    Optional<RefreshToken> findByMember_MemberId(Integer memberId);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.member.memberId = :memberId AND r.revoked = false")
    void revokeAllAdminTokens(Integer memberId);
    /*@Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expiredAt < CURRENT_TIMESTAMP")
    void deleteAllExpiredTokens();*/
}
