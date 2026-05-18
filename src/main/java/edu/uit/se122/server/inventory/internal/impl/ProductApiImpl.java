package edu.uit.se122.server.inventory.internal.impl;

import edu.uit.se122.server.booking.OrderContract;
import edu.uit.se122.server.inventory.ProductApi;
import edu.uit.se122.server.inventory.internal.entity.Product;
import edu.uit.se122.server.inventory.internal.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductApiImpl implements ProductApi {
    private final ProductRepository productRepository;

    @Override
    public void decreaseQuantity(List<OrderContract.ProductOrderDetailReq> list) {
        for (OrderContract.ProductOrderDetailReq detail : list) {
            if (detail.productCategoryId() == 1) continue;
            Product product = productRepository.findById(detail.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setQuantity(product.getQuantity() - detail.quantity());
        }
    }

    @Override
    public Integer getQuantity(Integer productId) {
        return productRepository.findById(productId).map(Product::getQuantity).orElse(0);
    }
}
