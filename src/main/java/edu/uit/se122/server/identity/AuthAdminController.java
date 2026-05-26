package edu.uit.se122.server.identity;

import edu.uit.se122.server.identity.internal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AuthAdminController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthContract.LoginAdminResponse> login(@RequestBody AuthContract.LoginAdminRequest dto) {
        return ResponseEntity.ok(authService.loginAdmin(dto));
    }
}
