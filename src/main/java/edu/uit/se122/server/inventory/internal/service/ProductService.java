package edu.uit.se122.server.inventory.internal.service;

import edu.uit.se122.server.common.enums.ProductStatus;
import edu.uit.se122.server.inventory.ProductContract;
import edu.uit.se122.server.inventory.internal.entity.Product;
import edu.uit.se122.server.inventory.internal.entity.ProductCategory;
import edu.uit.se122.server.inventory.internal.entity.ProductDetail;
import edu.uit.se122.server.inventory.internal.repository.ProductCategoryRepository;
import edu.uit.se122.server.inventory.internal.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public List<ProductContract.Res> getAll() {
        return productRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ProductContract.Res getById(Integer id) {
        return productRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void create(ProductContract.CreateReq dto) {
        ProductCategory category = productCategoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Integer maxId = productRepository.findMaxProductIdByCategoryId(category.getProductCategoryId());
        int newDetailId;
        if (maxId == null) {
            newDetailId = category.getProductCategoryId() * 1000 + 1;
        } else {
            newDetailId = maxId + 1;
        }

        Product product = new Product();
        product.setProductId(newDetailId);
        product.setName(dto.productName());
        product.setCategory(category);
        product.setStatus(ProductStatus.Available);
        product.setQuantity(0);
        product.setMinQuantity(0);
        ProductDetail detail = new ProductDetail();
        detail.setBarcode(dto.detail().barcode());
        detail.setCapacity(dto.detail().capacity());
        detail.setUnit(dto.detail().unit());
        detail.setUnitPrice(dto.detail().unitPrice());
        detail.setSaleType(dto.detail().saleType());
        product.addDetail(detail);
        Product saved = productRepository.save(product);

        ProductContract.CreatedEvent event = new ProductContract.CreatedEvent(
                saved.getProductId(),
                saved.getCategory().getProductCategoryId(),
                detail.getBarcode(),
                detail.getCapacity(),
                saved.getName(),
                detail.getUnit(),
                detail.getUnitPrice()
        );
        eventPublisher.publishEvent(event);
    }

    public void update(Integer id, ProductContract.UpdateReq dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.productName());
        updateDetailEntity(product, dto.details());
        productRepository.save(product);
    }

    public void updateQuantity(Integer id, ProductContract.UpdateQuantityReq dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Integer newQuantity = product.getQuantity() + dto.quantity();
        product.setQuantity(newQuantity);
        productRepository.save(product);
    }

    public void delete(Integer id) { productRepository.deleteById(id); }

    private void updateDetailEntity(Product entity, List<ProductContract.DetailUpdateReq> dtoList) {
        Map<Integer, ProductDetail> existingDetailsMap = entity.getDetails().stream()
                .collect(Collectors.toMap(ProductDetail::getProductDetailId, d -> d));

        for (ProductContract.DetailUpdateReq dto : dtoList) {
            ProductDetail detail;
            if (dto.productDetailId() != null && existingDetailsMap.containsKey(dto.productDetailId())) {
                detail = existingDetailsMap.get(dto.productDetailId());
                detail.setBarcode(dto.barcode());
                detail.setCapacity(dto.capacity());
                detail.setUnit(dto.unit());
                detail.setUnitPrice(dto.unitPrice());
                detail.setSaleType(dto.saleType());
            } else {
                detail = new ProductDetail();
                detail.setBarcode(dto.barcode());
                detail.setCapacity(dto.capacity());
                detail.setUnit(dto.unit());
                detail.setUnitPrice(dto.unitPrice());
                detail.setSaleType(dto.saleType());
                entity.addDetail(detail);
            }
        }
    }

    private ProductContract.Res mapToDTO(Product entity) {
        return new ProductContract.Res(
                entity.getProductId(),
                entity.getName(),
                entity.getStatus(),
                entity.getQuantity(),
                entity.getMinQuantity(),
                entity.getCategory().getProductCategoryId(),
                entity.getCategory().getName(),
                entity.getDetails().stream().map(d -> new ProductContract.DetailRes(
                        d.getProductDetailId(),
                        d.getBarcode(),
                        d.getCapacity(),
                        d.getUnit(),
                        d.getUnitPrice(),
                        d.getSaleType()
                )).toList()
        );
    }
}
