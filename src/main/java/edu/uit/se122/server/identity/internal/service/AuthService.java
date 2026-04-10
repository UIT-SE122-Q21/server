package edu.uit.se122.server.identity.internal.service;

import edu.uit.se122.server.common.enums.AdminRole;
import edu.uit.se122.server.common.enums.LoginRole;
import edu.uit.se122.server.common.enums.TokenType;
import edu.uit.se122.server.common.security.JwtService;
import edu.uit.se122.server.identity.AuthContract;
import edu.uit.se122.server.identity.internal.entity.Administrator;
import edu.uit.se122.server.identity.internal.entity.Member;
import edu.uit.se122.server.identity.internal.entity.MemberToken;
import edu.uit.se122.server.identity.internal.entity.RefreshToken;
import edu.uit.se122.server.identity.internal.repository.AdministratorRepository;
import edu.uit.se122.server.identity.internal.repository.MemberRepository;
import edu.uit.se122.server.identity.internal.repository.MemberTokenRepository;
import edu.uit.se122.server.identity.internal.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AdministratorRepository administratorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.base-url}")
    private String baseUrl;

    public void registerAdmin(AuthContract.RegisterAdminRequest dto) {
        Administrator admin = new Administrator();
        updateAdminEntity(admin, dto);
    }

    public void registerMember(AuthContract.RegisterMemberRequest dto) {
        Integer maxId = memberRepository.findMaxMemberId();
        int newMemberId;
        if (maxId == null) {
            newMemberId = 636 * 1000 + 1;
        } else {
            newMemberId = maxId + 1;
        }

        Member member = new Member();
        updateMemberEntity(member, dto);
        member.setVerified(false);
        member.setMemberId(newMemberId);
        Member savedMember = memberRepository.save(member);

        String token = UUID.randomUUID().toString();
        MemberToken memberToken = new MemberToken(token, savedMember);
        memberToken.setType(TokenType.EMAIL_VERIFICATION);
        memberTokenRepository.save(memberToken);

        String verifyLink = baseUrl + "/api/auth/verify?token=" + token;
        emailService.sendVerificationEmail(savedMember.getEmail(), verifyLink);
    }

    public void verifyEmail(String token) {
        MemberToken memberToken = memberTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token không tồn tại hoặc không hợp lệ"));

        if (memberToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            memberTokenRepository.delete(memberToken);
            throw new RuntimeException("Đường link xác thực đã hết hạn");
        }

        Member member = memberToken.getMember();
        member.setVerified(true);
        memberRepository.save(member);
        memberTokenRepository.delete(memberToken);
    }

    public AuthContract.LoginAdminResponse loginAdmin(AuthContract.LoginAdminRequest dto) {
        Administrator admin = administratorRepository.findById(dto.adminId())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        if (!passwordEncoder.matches(dto.password(), admin.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }
        String jwtToken = jwtService.generateToken(admin.getAdminId().toString(), admin.getEmail(), LoginRole.ADMIN.toString());

        return new AuthContract.LoginAdminResponse(jwtToken, admin.getAdminId(), admin.getName());
    }

    public AuthContract.LoginMemberResponse loginMember(AuthContract.LoginMemberRequest dto) {
        Member member = memberRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (!passwordEncoder.matches(dto.password(), member.getPasswordHash())) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }
        String accessToken = jwtService.generateToken(member.getMemberId().toString(), member.getEmail(), LoginRole.MEMBER.toString());
        RefreshToken refreshToken = refreshTokenService.create(member.getMemberId());

        return new AuthContract.LoginMemberResponse(
                accessToken,
                refreshToken.getToken(),
                member.getName(),
                member.getEmail()
        );
    }

    public AuthContract.LoginMemberResponse refreshToken(AuthContract.RefreshTokenRequest dto) {
        RefreshToken oldRefreshToken = refreshTokenRepository.findByToken(dto.refreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        refreshTokenService.verifyExpiration(oldRefreshToken);
        Member member = oldRefreshToken.getMember();
        String newAccessToken = jwtService.generateToken(
                member.getMemberId().toString(),
                member.getEmail(),
                LoginRole.MEMBER.toString()
        );
        RefreshToken newRefreshToken = refreshTokenService.create(member.getMemberId());

        return new AuthContract.LoginMemberResponse(
                newAccessToken,
                newRefreshToken.getToken(),
                member.getName(),
                member.getEmail()
        );
    }

    private void updateAdminEntity(Administrator entity, AuthContract.RegisterAdminRequest dto) {
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setPassword(passwordEncoder.encode(dto.password()));
        entity.setRole(dto.role() != null ? dto.role() : AdminRole.Manager);
    }

    public void updateMemberEntity(Member entity, AuthContract.RegisterMemberRequest dto) {
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setPasswordHash(passwordEncoder.encode(dto.password()));
    }
}
