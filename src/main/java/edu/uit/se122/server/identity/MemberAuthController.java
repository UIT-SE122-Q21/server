package edu.uit.se122.server.identity;

import edu.uit.se122.server.identity.internal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberAuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AuthContract.RegisterMemberRequest dto) {
        authService.registerMember(dto);
        return ResponseEntity.ok("Đăng ký thành công");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthContract.LoginMemberResponse> login(@RequestBody AuthContract.LoginMemberRequest dto) {
        return ResponseEntity.ok(authService.loginMember(dto));
    }
}
