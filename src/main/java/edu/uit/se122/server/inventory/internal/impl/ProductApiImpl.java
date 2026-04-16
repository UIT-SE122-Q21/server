package edu.uit.se122.server.inventory.internal.impl;

import edu.uit.se122.server.booking.ProductOrderContract;
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
    public void DecreaseQuantity(List<ProductOrderContract.DetailRequest> list) {
        for (ProductOrderContract.DetailRequest detailRequest : list) {
            Product product = productRepository.findById(detailRequest.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            int newQuantity = product.getQuantity() - detailRequest.quantity();
            product.setQuantity(newQuantity);
            productRepository.save(product);
        }
    }
}
