package edu.uit.se122.server.inventory.internal.service;

import edu.uit.se122.server.inventory.ProductContract;
import edu.uit.se122.server.inventory.internal.entity.Product;
import edu.uit.se122.server.inventory.internal.entity.ProductCategory;
import edu.uit.se122.server.inventory.internal.repository.ProductCategoryRepository;
import edu.uit.se122.server.inventory.internal.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductContract.Response> getAll() {
        return productRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ProductContract.Response getById(Integer id) {
        return productRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void create(ProductContract.Request dto) {
        ProductCategory category = productCategoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Integer maxId = productRepository.findMaxProductIdByCategoryId(category.getProductCategoryId());
        int newProductId;
        if (maxId == null) {
            newProductId = category.getProductCategoryId() * 1000 + 1;
        } else {
            newProductId = maxId + 1;
        }

        Product product = new Product();
        product.setProductId(newProductId);
        updateEntity(product, dto);
        Product saved = productRepository.save(product);
    }

    public void update(Integer id, ProductContract.Request dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        updateEntity(product, dto);
        productRepository.save(product);
    }

    public void delete(Integer id) { productRepository.deleteById(id); }

    private void updateEntity(Product entity, ProductContract.Request dto) {
        entity.setBarcode(dto.barcode());
        entity.setName(dto.productName());
        entity.setUnitPrice(dto.unitPrice());
        entity.setQuantity(dto.quantity());
        entity.setMinQuantity(dto.minQuantity());
        entity.setStatus(dto.status());
        ProductCategory category = productCategoryRepository.findById(dto.categoryId()).orElseThrow();
        entity.setCategory(category);
    }

    private ProductContract.Response mapToDTO(Product entity) {
        String formattedProductId = String.format("%06d", entity.getProductId());
        return new ProductContract.Response(
                formattedProductId,
                entity.getBarcode(),
                entity.getName(),
                entity.getUnitPrice(),
                entity.getQuantity(),
                entity.getMinQuantity(),
                entity.getStatus(),
                entity.getCategory().getName()
        );
    }
}
