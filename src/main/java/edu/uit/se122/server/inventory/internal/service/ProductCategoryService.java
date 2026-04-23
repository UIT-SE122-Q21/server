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

    public List<ProductCategoryContract.Res> getAll() {
        return productCategoryRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ProductCategoryContract.Res getById(Integer id) {
        return productCategoryRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void create(ProductCategoryContract.Req dto) {
        ProductCategory category = new ProductCategory();
        updateEntity(category, dto);
        ProductCategory saved = productCategoryRepository.save(category);
    }

    public void update(Integer id, ProductCategoryContract.Req dto) {
        ProductCategory category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        updateEntity(category, dto);
        productCategoryRepository.save(category);
    }

    public void delete(Integer id) { productCategoryRepository.deleteById(id); }

    private void updateEntity(ProductCategory entity, ProductCategoryContract.Req dto) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setBackgroundColor(dto.backgroundColor());
        entity.setTextColor(dto.textColor());
    }

    private ProductCategoryContract.Res mapToDTO(ProductCategory entity) {
        String formattedCategoryId = String.format("%03d", entity.getProductCategoryId());
        return new ProductCategoryContract.Res(
                formattedCategoryId,
                entity.getName(),
                entity.getDescription(),
                entity.getBackgroundColor(),
                entity.getTextColor()
        );
    }
}
