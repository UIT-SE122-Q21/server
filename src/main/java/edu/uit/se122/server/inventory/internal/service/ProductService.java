package edu.uit.se122.server.inventory.internal.service;

import edu.uit.se122.server.common.enums.ProductStatus;
import edu.uit.se122.server.inventory.ProductContract;
import edu.uit.se122.server.inventory.internal.entity.Product;
import edu.uit.se122.server.inventory.internal.entity.ProductCategory;
import edu.uit.se122.server.inventory.internal.entity.ProductDetail;
import edu.uit.se122.server.inventory.internal.repository.ProductCategoryRepository;
import edu.uit.se122.server.inventory.internal.repository.ProductDetailRepository;
import edu.uit.se122.server.inventory.internal.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductDetailRepository detailRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public List<ProductContract.Res> getAll() {
        return productRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ProductContract.Res getById(Integer id) {
        return productRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void create(ProductContract.Req dto) {
        ProductCategory category = productCategoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Integer maxId = detailRepository.findMaxProductDetailIdByCategoryId(category.getProductCategoryId());
        int newDetailId;
        newDetailId = Objects.requireNonNullElseGet(maxId, () -> category.getProductCategoryId() * 1000);

        Product product = new Product();
        updateEntity(product, dto, category, newDetailId);
        Product saved = productRepository.save(product);

        for (ProductDetail detail : saved.getDetails()) {
            ProductContract.CreatedEvent event = new ProductContract.CreatedEvent(
                    detail.getProductDetailId(),
                    detail.getBarcode(),
                    saved.getName(),
                    detail.getUnitPrice()
            );
            eventPublisher.publishEvent(event);
        }
    }

    public void update(Integer id, ProductContract.UpdateReq dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        ProductCategory category = productCategoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Integer maxId = detailRepository.findMaxProductDetailIdByCategoryId(category.getProductCategoryId());
        int newDetailId;
        newDetailId = Objects.requireNonNullElseGet(maxId, () -> category.getProductCategoryId() * 1000);

        product.setName(dto.productName());
        product.setCapacity(dto.capacity());
        product.setCategory(category);
        updateDetailEntity(product, dto.details(), newDetailId);
        productRepository.save(product);
    }

    public void delete(Integer id) { productRepository.deleteById(id); }

    private void updateEntity(Product entity, ProductContract.Req dto, ProductCategory category, Integer maxId) {
        AtomicInteger indexOfDetails = new AtomicInteger(1);
        
        entity.setName(dto.productName());
        entity.setCapacity(dto.capacity());
        entity.setCategory(category);
        List<ProductDetail> details = dto.details().stream().map(detailReq -> {
            ProductDetail detail = new ProductDetail();
            detail.setProductDetailId(maxId + indexOfDetails.get());
            detail.setBarcode(detailReq.barcode());
            detail.setUnit(detailReq.unit());
            detail.setUnitPrice(detailReq.unitPrice());
            detail.setSaleType(detailReq.saleType());
            detail.setQuantity(0);
            detail.setMinQuantity(0);
            detail.setStatus(ProductStatus.Available);
            detail.setProduct(entity);
            indexOfDetails.getAndIncrement();
            return detail;
        }).toList();
        entity.setDetails(details);
    }

    private void updateDetailEntity(Product entity, List<ProductContract.DetailUpdateReq> detailUpdateReqs, Integer maxId) {
        Map<Integer, ProductDetail> existingDetailsMap = entity.getDetails().stream()
                .collect(Collectors.toMap(ProductDetail::getProductDetailId, d -> d));
        AtomicInteger indexOfDetails = new AtomicInteger(1);

        for (ProductContract.DetailUpdateReq updateReq : detailUpdateReqs) {
            ProductDetail detail;
            if (updateReq.productDetailId() != null && existingDetailsMap.containsKey(updateReq.productDetailId())) {
                detail = existingDetailsMap.get(updateReq.productDetailId());
                detail.setBarcode(updateReq.barcode());
                detail.setUnit(updateReq.unit());
                detail.setUnitPrice(updateReq.unitPrice());
                detail.setSaleType(updateReq.saleType());
                detail.setStatus(updateReq.status());
            } else {
                detail = new ProductDetail();
                detail.setProductDetailId(maxId + indexOfDetails.get());
                detail.setBarcode(updateReq.barcode());
                detail.setUnit(updateReq.unit());
                detail.setUnitPrice(updateReq.unitPrice());
                detail.setSaleType(updateReq.saleType());
                detail.setQuantity(0);
                detail.setMinQuantity(0);
                detail.setStatus(ProductStatus.Available);
                detail.setProduct(entity);
                entity.getDetails().add(detail);
                indexOfDetails.getAndIncrement();
            }
        }
    }

    private ProductContract.Res mapToDTO(Product entity) {
        return new ProductContract.Res(
                entity.getProductId(),
                entity.getName(),
                entity.getCapacity(),
                entity.getCategory().getProductCategoryId(),
                entity.getCategory().getName(),
                entity.getDetails().stream().map(d -> new ProductContract.DetailRes(
//                        String.format("%06d", d.getProductDetailId()),
                        d.getProductDetailId(),
                        d.getBarcode(),
                        d.getUnit(),
                        d.getUnitPrice(),
                        d.getSaleType(),
                        d.getQuantity(),
                        d.getMinQuantity(),
                        d.getStatus()
                )).toList()
        );
    }
}
