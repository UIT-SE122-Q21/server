package edu.uit.se122.server.identity.internal.service;

import edu.uit.se122.server.common.enums.AdminRole;
import edu.uit.se122.server.common.enums.LoginRole;
import edu.uit.se122.server.common.security.JwtService;
import edu.uit.se122.server.identity.AuthContract;
import edu.uit.se122.server.identity.internal.entity.Administrator;
import edu.uit.se122.server.identity.internal.entity.Member;
import edu.uit.se122.server.identity.internal.repository.AdministratorRepository;
import edu.uit.se122.server.identity.internal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AdministratorRepository administratorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    public void registerAdmin(AuthContract.RegisterAdminRequest dto) {
        Administrator admin = new Administrator();
        updateAdminEntity(admin, dto);
    }

    public void registerMember(AuthContract.RegisterMemberRequest dto) {
        Member member = new Member();
        updateMemberEntity(member, dto);
    }

    public AuthContract.LoginAdminResponse loginAdmin(AuthContract.LoginAdminRequest dto) {
        Administrator admin = administratorRepository.findById(dto.adminId())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        if (!passwordEncoder.matches(dto.password(), admin.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }
        String jwtToken = jwtService.generateToken(admin.getAdminId().toString(), admin.getEmail(), LoginRole.ADMIN);

        return new AuthContract.LoginAdminResponse(jwtToken, admin.getAdminId(), admin.getName());
    }

    public AuthContract.LoginMemberResponse loginMember(AuthContract.LoginMemberRequest dto) {
        Member member = memberRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (!passwordEncoder.matches(dto.password(), member.getPasswordHash())) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }
        String jwtToken = jwtService.generateToken(member.getMemberId().toString(), member.getEmail(), LoginRole.MEMBER);

        return new AuthContract.LoginMemberResponse(jwtToken, member.getName(), member.getEmail());
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
