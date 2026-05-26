package edu.uit.se122.server.identity.internal.service;

import edu.uit.se122.server.common.security.JwtProperties;
import edu.uit.se122.server.identity.internal.entity.Member;
import edu.uit.se122.server.identity.internal.entity.RefreshToken;
import edu.uit.se122.server.identity.internal.repository.MemberRepository;
import edu.uit.se122.server.identity.internal.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtProperties jwtProperties;

    public RefreshToken create(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByMember(member);
        RefreshToken refreshToken;

        if (existingToken.isPresent()) {
            refreshToken = existingToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setRevoked(false);
            refreshToken.setExpiredAt(Instant.now().plusMillis(jwtProperties.getRefreshExpiration()));
            return refreshTokenRepository.save(refreshToken);
        } else {
            // 2. Tạo Token mới dạng UUID
            refreshToken = RefreshToken.builder()
                    .member(member) // Gán Object Member vào đây
                    .token(UUID.randomUUID().toString())
                    .expiredAt(Instant.now().plusMillis(jwtProperties.getRefreshExpiration()))
                    .revoked(false)
                    .build();
        }

        return refreshTokenRepository.save(refreshToken);
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.isRevoked()) {
            throw new RuntimeException("Refresh Token đã bị thu hồi. Vui lòng đăng nhập lại.");
        }

        if (token.getExpiredAt().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh Token đã hết hạn. Vui lòng đăng nhập lại.");
        }
    }
}
