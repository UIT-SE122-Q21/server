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
    public void DecreaseQuantity(List<ProductOrderContract.DetailReq> list) {
        for (ProductOrderContract.DetailReq detailReq : list) {
            ProductDetail detail = detailRepository.findById(detailReq.productDetailId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            int newQuantity = detail.getQuantity() - detailReq.quantity();
            detail.setQuantity(newQuantity);
            detailRepository.save(detail);
        }
    }
}
