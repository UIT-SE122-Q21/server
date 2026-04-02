package edu.uit.se122.server.promotion.internal.service;

import edu.uit.se122.server.promotion.internal.dto.PromotionReqDTO;
import edu.uit.se122.server.promotion.internal.dto.PromotionResDTO;
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
    public PromotionResDTO create(PromotionReqDTO dto) {
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
            List<PromotionDetail> savedDetails = detailRepository.saveAll(details);
            saved.setDetails(savedDetails);
        }
        return mapToDTO(saved);
    }

    // READ ALL
    public List<PromotionResDTO> getAll() {
        return promotionRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    // READ ONE
    public PromotionResDTO getById(Integer id) {
        return promotionRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    // UPDATE
    public PromotionResDTO update(Integer id, PromotionReqDTO dto) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        updateEntity(promotion, dto);
        promotionRepository.save(promotion);
        return promotionRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    // DELETE
    public void delete(Integer id) {
        promotionRepository.deleteById(id);
    }

    // Helper mappers
    private void updateEntity(Promotion entity, PromotionReqDTO dto) {
        entity.setPromotionName(dto.getPromotionName());
        entity.setDescription(dto.getDescription());
        entity.setDiscountType(dto.getDiscountType());
        entity.setDiscountValue(dto.getDiscountValue());
    }

    private PromotionResDTO mapToDTO(Promotion entity) {
        PromotionResDTO dto = new PromotionResDTO();
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
