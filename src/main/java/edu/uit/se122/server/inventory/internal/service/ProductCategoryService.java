package edu.uit.se122.server.inventory.internal.service;

import edu.uit.se122.server.inventory.internal.dto.ProductCategoryReqDTO;
import edu.uit.se122.server.inventory.internal.dto.ProductCategoryResDTO;
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

    public List<ProductCategoryResDTO> getAll() {
        return productCategoryRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ProductCategoryResDTO getById(Integer id) {
        return productCategoryRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public ProductCategoryResDTO create(ProductCategoryReqDTO dto) {
        ProductCategory category = new ProductCategory();
        updateEntity(category, dto);
        ProductCategory saved = productCategoryRepository.save(category);

        return mapToDTO(saved);
    }

    public ProductCategoryResDTO update(Integer id, ProductCategoryReqDTO dto) {
        ProductCategory category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        updateEntity(category, dto);
        productCategoryRepository.save(category);

        return productCategoryRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void delete(Integer id) { productCategoryRepository.deleteById(id); }

    private void updateEntity(ProductCategory entity, ProductCategoryReqDTO dto) {
        entity.setProductCategoryName(dto.getProductCategoryName());
        entity.setDescription(dto.getDescription());
    }

    private ProductCategoryResDTO mapToDTO(ProductCategory entity) {
        ProductCategoryResDTO dto = new ProductCategoryResDTO();
        dto.setProductCategoryId(entity.getProductCategoryId());
        dto.setProductCategoryName(entity.getProductCategoryName());
        dto.setDescription(entity.getDescription());

        return dto;
    }
}
