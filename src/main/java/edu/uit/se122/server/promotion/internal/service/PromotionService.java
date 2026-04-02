package edu.uit.se122.server.promotion.internal.service;

import edu.uit.se122.server.promotion.PromotionDTO;
import edu.uit.se122.server.promotion.internal.entity.Promotion;
import edu.uit.se122.server.promotion.internal.entity.PromotionDetail;
import edu.uit.se122.server.promotion.internal.repository.PromotionDetailRepository;
import edu.uit.se122.server.promotion.internal.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionDetailRepository detailRepository;

    // CREATE
    public PromotionDTO create(PromotionDTO dto) {
        Promotion promotion = new Promotion();
        updateEntity(promotion, dto);
        Promotion saved = promotionRepository.save(promotion);

        // Lưu chi tiết sản phẩm áp dụng
        if (dto.getProductIds() != null) {
            List<PromotionDetail> details = dto.getProductIds().stream().map(pid -> {
                PromotionDetail detail = new PromotionDetail();
                detail.setPromotion(saved);
                detail.setProductId(pid); // Chỉ lưu ID theo chuẩn Modulith
                return detail;
            }).collect(Collectors.toList());
            detailRepository.saveAll(details);
        }

        dto.setPromotionId(saved.getPromotionId());
        return dto;
    }

    // READ ALL
    public List<PromotionDTO> getAll() {
        return promotionRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    // READ ONE
    public PromotionDTO getById(Integer id) {
        return promotionRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    // UPDATE
    public PromotionDTO update(Integer id, PromotionDTO dto) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        updateEntity(promotion, dto);
        promotionRepository.save(promotion);
        return dto;
    }

    // DELETE
    public void delete(Integer id) {
        promotionRepository.deleteById(id);
    }

    // Helper mappers
    private void updateEntity(Promotion entity, PromotionDTO dto) {
        entity.setPromotionName(dto.getPromotionName());
        entity.setDescription(dto.getDescription());
        entity.setDiscountType(dto.getDiscountType());
        entity.setDiscountValue(dto.getDiscountValue());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
    }

    private PromotionDTO mapToDTO(Promotion entity) {
        PromotionDTO dto = new PromotionDTO();
        dto.setPromotionId(entity.getPromotionId());
        dto.setPromotionName(entity.getPromotionName());
        dto.setDescription(entity.getDescription());
        dto.setDiscountType(entity.getDiscountType());
        dto.setDiscountValue(entity.getDiscountValue());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        return dto;
    }
}
