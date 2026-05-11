package edu.uit.se122.server.booking;

import edu.uit.se122.server.booking.internal.service.ProductOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/order/product")
@RequiredArgsConstructor
public class ProductOrderAdminController {
    private final ProductOrderService productOrderService;

    @GetMapping("/calculate")
    public ResponseEntity<ProductOrderContract.CalculateInvoiceRes> calculateInvoice(@RequestBody ProductOrderContract.InvoiceReq dto) {
        return ResponseEntity.ok(productOrderService.calculateInvoice(dto));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ProductOrderContract.InvoiceReq dto) {
        productOrderService.createInvoice(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }
}
