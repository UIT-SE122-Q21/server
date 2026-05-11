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
@RequestMapping("/api/customer/order/product")
@RequiredArgsConstructor
public class ProductOrderCustomerController {
    private final ProductOrderService productOrderService;

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody ProductOrderContract.CreateOrderByCustomer dto) {
        productOrderService.createOrderByCustomer(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }
}
