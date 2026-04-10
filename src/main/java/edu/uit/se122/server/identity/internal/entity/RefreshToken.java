package edu.uit.se122.server.identity.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "member_refresh_token", indexes = {
        @Index(name = "idx_member_rt_token", columnList = "token")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @Column(nullable = false, unique = true, length = 100)
    private String token; // Lưu chuỗi UUID

    @Column(nullable = false)
    private Instant expiredAt;

    // Cờ này dùng để vô hiệu hóa Token khi người dùng nhấn "Đăng xuất"
    @Column(nullable = false)
    private boolean revoked = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    @JsonBackReference(value = "refreshToken")
    private Member member;
}
