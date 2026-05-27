package edu.uit.se122.server.resource.internal.component;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class PriceManager {
    // Khởi tạo giá mặc định ban đầu là 150,000
    private final AtomicReference<CourtPrice> currentPriceHolder =
            new AtomicReference<>(new CourtPrice(BigDecimal.valueOf(30000)));

    // Lấy đối tượng Flyweight hiện tại
    public CourtPrice getCurrentCourtPrice() {
        return currentPriceHolder.get();
    }

    // Thay thế đối tượng Flyweight cũ bằng đối tượng Flyweight mới
    public void updateCourtPrice(CourtPrice newConfig) {
        currentPriceHolder.set(newConfig);
    }
}
