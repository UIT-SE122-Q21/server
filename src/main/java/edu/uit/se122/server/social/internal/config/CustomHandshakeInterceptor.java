package edu.uit.se122.server.social.internal.config;

import edu.uit.se122.server.common.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class CustomHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    private final JwtService jwtService;

    public CustomHandshakeInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

            // Lấy token từ URL Parameter (Ví dụ: ws://localhost:8080/ws-chat?token=eyJhbGciOi...)
            String token = servletRequest.getParameter("token");

            if (token != null && jwtService.isTokenValid(token)) {
                // Giải mã lấy ID người dùng từ JWT
                int memberId = Integer.parseInt(jwtService.extractId(token));

                // ĐÚT VÀO ATTRIBUTES: Dữ liệu này sẽ đi theo toàn bộ phiên (Session) làm việc của Socket
                attributes.put("memberId", String.valueOf(memberId));

                return true; // Cho phép bắt tay thành công
            }
        }

        // Nếu không có token hoặc token lỏ, từ chối kết nối ngay từ vòng gửi xe (Trả về 401/403)
        return false;
    }
}
