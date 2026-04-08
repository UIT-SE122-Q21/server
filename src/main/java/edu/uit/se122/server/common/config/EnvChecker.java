package edu.uit.se122.server.common.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j // Dùng thư viện log của Lombok
public class EnvChecker {

    @Value("${spring.mail.username}")
    private String brevoEmail;

    @Value("${spring.mail.password}")
    private String brevoPassword;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @PostConstruct
    public void checkEnvVariables() {
        log.info("=== KIỂM TRA BIẾN MÔI TRƯỜNG ===");
        log.info("Brevo Email đã load: {}", brevoEmail);

        // LƯU Ý: Không bao giờ in toàn bộ Password/Secret ra log thực tế.
        // Chỉ in ra độ dài hoặc vài ký tự đầu để biết nó đã có dữ liệu hay chưa.
        log.info("Brevo Password (Length): {}", brevoPassword != null ? brevoPassword.length() : "NULL");
        log.info("JWT Secret (Length): {}", jwtSecret != null ? jwtSecret.length() : "NULL");
        log.info("=================================");
    }
}
