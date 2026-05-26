package edu.uit.se122.server.booking;

import edu.uit.se122.server.booking.internal.service.CourtOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class OrderAdminController {
    private final CourtOrderService courtOrderService;

    @GetMapping
    public ResponseEntity<List<OrderContract.ResByAdmin>> getAll() {
        return ResponseEntity.ok(courtOrderService.getAllByAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderContract.ResByAdmin> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(courtOrderService.getById(id));
    }

    @PostMapping
    public ResponseEntity<OrderContract.CreatedOrderRes> createOrder(
            @AuthenticationPrincipal Integer adminId,
            @RequestBody OrderContract.CreateOrderByAdminReq dto
    ) {
        return ResponseEntity.ok(courtOrderService.createOrderByAdmin(adminId, dto));
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

    @PostMapping("/court/schedule")
    public ResponseEntity<Map<Integer, List<OrderContract.CourtRes>>> getAllCourtSchedule(@RequestBody OrderContract.CourtReq dto) {
        return ResponseEntity.ok(courtOrderService.getAllCourtSchedule(dto));
    }
}
