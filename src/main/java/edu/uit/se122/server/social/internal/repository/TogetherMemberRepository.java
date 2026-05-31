package edu.uit.se122.server.social.internal.repository;

import edu.uit.se122.server.social.internal.entity.TogetherMember;
import edu.uit.se122.server.social.internal.entity.TogetherMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TogetherMemberRepository extends JpaRepository<TogetherMember, TogetherMemberId> {
    @Query("""
            SELECT COUNT(tm) > 0
            FROM TogetherMember tm
            WHERE tm.id.togetherId = :togetherId
              AND tm.id.memberId = :memberId
            """)
    boolean checkMemberInTogether(
            @Param("togetherId") Integer togetherId,
            @Param("memberId") Integer memberId
    );
}
