package edu.uit.se122.server.identity;

import edu.uit.se122.server.identity.internal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberAuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AuthContract.RegisterMemberRequest dto) {
        authService.registerMember(dto);
        return ResponseEntity.ok(Map.of("message", "Đăng ký thành công"));
    }

    @GetMapping("/verify")
    public ResponseEntity<Object> verifyEmail(@RequestParam("token") String token) {
        try {
            authService.verifyEmail(token);
            return ResponseEntity.ok(Map.of("message", "Xác thực tài khoản thành công! Bạn có thể đăng nhập ngay bây giờ."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*@GetMapping("/verify")
    public void verifyEmail(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        // Giả sử giao diện Frontend của bạn đang chạy ở cổng 3000
        String frontendLoginUrl = "http://localhost:3000/login";

        try {
            authService.verifyEmail(token);
            // Redirect thành công
            response.sendRedirect(frontendLoginUrl + "?verify=success");
        } catch (RuntimeException e) {
            // Redirect kèm thông báo lỗi
            response.sendRedirect(frontendLoginUrl + "?verify=error&message=" + e.getMessage());
        }
    }*/

    @PostMapping("/login")
    public ResponseEntity<AuthContract.LoginMemberResponse> login(@RequestBody AuthContract.LoginMemberRequest dto) {
        return ResponseEntity.ok(authService.loginMember(dto));
    }
}
