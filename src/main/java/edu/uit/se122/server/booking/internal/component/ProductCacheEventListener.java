package edu.uit.se122.server.booking.internal.component;

import edu.uit.se122.server.booking.internal.entity.ProductCache;
import edu.uit.se122.server.booking.internal.repository.ProductCacheRepository;
import edu.uit.se122.server.inventory.ProductContract;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductCacheEventListener {
    private final ProductCacheRepository cacheRepository;

    @ApplicationModuleListener
    void onProductUpdated(ProductContract.CreatedEvent dto) {
        ProductCache productCache = new ProductCache();
        productCache.setProductId(dto.productId());
        productCache.setProductCategoryId(dto.productCategoryId());
        productCache.setBarcode(dto.barcode());
        productCache.setCapacity(dto.capacity());
        productCache.setName(dto.name());
        productCache.setUnit(dto.unit());
        productCache.setUnitPrice(dto.unitPrice());
        cacheRepository.save(productCache);
    }
}
