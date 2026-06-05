package edu.uit.se122.server.booking;

import edu.uit.se122.server.booking.internal.service.ZaloPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final ZaloPayService zaloPayService;

    @PostMapping("/{id}")
    public ResponseEntity<ZaloPayContract.OrderRes> createPayment(@PathVariable Integer id, @RequestBody OrderContract.CreateInvoiceByAdminReq dto) {
        return ResponseEntity.ok(zaloPayService.createPayment(id, dto));
    }

    @PostMapping("/callback")
    public ResponseEntity<ZaloPayContract.CallbackStatusRes> handlePaymentCallback(@RequestBody ZaloPayContract.Callback callback) {
        return ResponseEntity.ok(zaloPayService.handlePaymentCallback(callback));
    }
}
