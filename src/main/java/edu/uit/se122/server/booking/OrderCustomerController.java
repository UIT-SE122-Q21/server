package edu.uit.se122.server.booking;

import edu.uit.se122.server.booking.internal.service.CourtOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer/order")
@RequiredArgsConstructor
public class OrderCustomerController {
    private final CourtOrderService courtOrderService;

    @GetMapping
    public ResponseEntity<List<OrderContract.ResByCustomer>> getAll(@AuthenticationPrincipal Integer userId) {
        return ResponseEntity.ok(courtOrderService.getAllByCustomer(userId));
    }

    @PostMapping
    public ResponseEntity<OrderContract.CreatedOrderRes> create(
            @AuthenticationPrincipal Integer userId,
            @RequestBody OrderContract.CreateOrderByCustomerReq dto
    ) {
        return ResponseEntity.ok(courtOrderService.createOrderByCustomer(userId, dto));
    }

    @PostMapping("/{id}/calculate")
    public ResponseEntity<OrderContract.CalculateDepositRes> calculateDeposit(@PathVariable Integer id) {
        return ResponseEntity.ok(courtOrderService.calculateDepositValue(id));
    }

    @PostMapping("/{id}/invoice")
    public ResponseEntity<Object> createInvoice(@PathVariable Integer id) {
        courtOrderService.createInvoiceByCustomer(id);
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
