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
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthContract.AdminBasic> register(@RequestBody AuthContract.RegisterRequest dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthContract.LoginResponse> login(@RequestBody AuthContract.LoginRequest dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
