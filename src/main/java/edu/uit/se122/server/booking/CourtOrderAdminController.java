package edu.uit.se122.server.booking;

import edu.uit.se122.server.booking.internal.service.CourtOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/order/court")
@RequiredArgsConstructor
public class CourtOrderAdminController {
    private final CourtOrderService courtOrderService;

    @PostMapping
    public ResponseEntity<Object> create(
            @AuthenticationPrincipal Integer adminId,
            @RequestBody CourtOrderContract.Request dto
    ) {
        courtOrderService.createByAdmin(adminId, dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }
}
