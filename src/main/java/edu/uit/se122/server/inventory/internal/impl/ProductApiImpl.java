package edu.uit.se122.server.inventory.internal.impl;

import edu.uit.se122.server.booking.ProductOrderContract;
import edu.uit.se122.server.inventory.ProductApi;
import edu.uit.se122.server.inventory.internal.entity.ProductDetail;
import edu.uit.se122.server.inventory.internal.repository.ProductDetailRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductApiImpl implements ProductApi {
    private final ProductDetailRepository detailRepository;

    @Override
    public void DecreaseQuantity(List<ProductOrderContract.DetailRequest> list) {
        for (ProductOrderContract.DetailRequest detailRequest : list) {
            ProductDetail detail = detailRepository.findById(detailRequest.productDetailId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            int newQuantity = detail.getQuantity() - detailRequest.quantity();
            detail.setQuantity(newQuantity);
            detailRepository.save(detail);
        }
    }
}
