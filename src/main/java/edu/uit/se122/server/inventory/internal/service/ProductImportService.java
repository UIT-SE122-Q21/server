package edu.uit.se122.server.inventory.internal.service;

import edu.uit.se122.server.common.enums.ImportStatus;
import edu.uit.se122.server.inventory.ProductImportContract;
import edu.uit.se122.server.inventory.internal.entity.ProductDetail;
import edu.uit.se122.server.inventory.internal.entity.ProductImport;
import edu.uit.se122.server.inventory.internal.repository.ProductDetailRepository;
import edu.uit.se122.server.inventory.internal.repository.ProductImportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImportService {
    private final ProductImportRepository importRepository;
    private final ProductDetailRepository detailRepository;

    public List<ProductImportContract.Res> getAll() {
        return importRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public void create(ProductImportContract.CreateReq dto) {
        ProductDetail detail = detailRepository.findById(dto.productDetailId())
                .orElseThrow(() -> new RuntimeException("Detail not found"));

        ProductImport productImport = new ProductImport();
        productImport.setQuantity(dto.quantity());
        productImport.setNote(dto.note());
        productImport.setStatus(ImportStatus.Pending);
        productImport.setDetail(detail);
        importRepository.save(productImport);
    }

    private ProductImportContract.Res mapToDTO(ProductImport entity) {
        return new ProductImportContract.Res(
                entity.getImportId(),
                entity.getQuantity(),
                entity.getNote(),
                entity.getStatus(),
                entity.getUpdateAt(),
                entity.getDetail().getProductDetailId(),
                entity.getDetail().getProduct().getName()
        );
    }
}
