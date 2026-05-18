package edu.uit.se122.server.booking;

import edu.uit.se122.server.booking.internal.service.CourtOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class OrderAdminController {
    private final CourtOrderService courtOrderService;

    @PostMapping("/court")
    public ResponseEntity<OrderContract.CreatedOrderRes> createOrder(
            @AuthenticationPrincipal Integer adminId,
            @RequestBody OrderContract.CreateOrderByAdminReq dto
    ) {
        return ResponseEntity.ok(courtOrderService.createOrderByAdmin(adminId, dto));
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderContract.OrderHistoryRes>> getOrderHistory() {
        return ResponseEntity.ok(courtOrderService.getOrderHistory());
    }

    @PostMapping("/{id}/calculate")
    public ResponseEntity<OrderContract.CalculateTotalRes> calculateTotalValue(@PathVariable Integer id, @RequestBody OrderContract.CreateInvoiceByAdminReq dto) {
        return ResponseEntity.ok(courtOrderService.calculateTotalValue(id, dto));
    }

    @PostMapping("/{id}/invoice")
    public ResponseEntity<Object> createInvoice(@PathVariable Integer id, @RequestBody OrderContract.CreateInvoiceByAdminReq dto) {
        courtOrderService.createInvoiceByAdmin(id, dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Object> updateProductOrderDetails(@PathVariable Integer id, @RequestBody List<OrderContract.ProductOrderDetailReq> dtoList) {
        courtOrderService.updateProductOrderDetails(id, dtoList);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }
}
