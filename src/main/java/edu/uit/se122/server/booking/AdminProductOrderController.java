package edu.uit.se122.server.booking;

import edu.uit.se122.server.booking.internal.service.ProductOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/order/product")
@RequiredArgsConstructor
public class AdminProductOrderController {
    private final ProductOrderService productOrderService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ProductOrderContract.InvoiceRequest dto) {
        productOrderService.calculateInvoiceByAdmin(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }
}
