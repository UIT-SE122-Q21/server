package edu.uit.se122.server.booking;

import edu.uit.se122.server.booking.internal.service.CourtOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer/order/court")
@RequiredArgsConstructor
public class CourtOrderCustomerController {
    private final CourtOrderService courtOrderService;

    @GetMapping
    public ResponseEntity<List<CourtOrderContract.Res>> getAll() {
        return ResponseEntity.ok(courtOrderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourtOrderContract.Res> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(courtOrderService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(
            @AuthenticationPrincipal Integer userId,
            @RequestBody CourtOrderContract.CreateByCustomerReq dto
    ) {
        courtOrderService.createByUser(userId, dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }
}
