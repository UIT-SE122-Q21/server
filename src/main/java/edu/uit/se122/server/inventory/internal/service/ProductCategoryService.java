package edu.uit.se122.server.inventory.internal.service;

import edu.uit.se122.server.inventory.ProductCategoryContract;
import edu.uit.se122.server.inventory.internal.entity.ProductCategory;
import edu.uit.se122.server.inventory.internal.repository.ProductCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductCategoryContract.Response> getAll() {
        return productCategoryRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ProductCategoryContract.Response getById(Integer id) {
        return productCategoryRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void create(ProductCategoryContract.Request dto) {
        ProductCategory category = new ProductCategory();
        updateEntity(category, dto);
        ProductCategory saved = productCategoryRepository.save(category);
    }

    public void update(Integer id, ProductCategoryContract.Request dto) {
        ProductCategory category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        updateEntity(category, dto);
        productCategoryRepository.save(category);
    }

    public void delete(Integer id) { productCategoryRepository.deleteById(id); }

    private void updateEntity(ProductCategory entity, ProductCategoryContract.Request dto) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
    }

    private ProductCategoryContract.Response mapToDTO(ProductCategory entity) {
        String formattedCategoryId = String.format("%03d", entity.getProductCategoryId());
        return new ProductCategoryContract.Response(
                formattedCategoryId,
                entity.getName(),
                entity.getDescription()
        );
    }
}
