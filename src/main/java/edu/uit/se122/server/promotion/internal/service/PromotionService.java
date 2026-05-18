package edu.uit.se122.server.promotion.internal.service;

import edu.uit.se122.server.promotion.PromotionContract;
import edu.uit.se122.server.promotion.internal.entity.Promotion;
import edu.uit.se122.server.promotion.internal.entity.PromotionDetail;
import edu.uit.se122.server.promotion.internal.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionService {
    private final PromotionRepository promotionRepository;

    // CREATE
    public void create(PromotionContract.CreateReq dto) {
        Promotion promotion = mapToPromotion(dto);
        List<PromotionDetail> detail = mapToDetail(promotion, dto.details());
        promotion.setDetails(detail);
        promotionRepository.save(promotion);
    }

    // READ ALL
    public List<PromotionContract.Res> getAll() {
        return promotionRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    // READ ONE
    public PromotionContract.Res getById(Integer id) {
        return promotionRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    public void hide(Integer id, PromotionContract.HideReq dto) {
        promotionRepository.findById(id).ifPresent(promotion -> promotion.setHidden(dto.hidden()));
    }

    private Promotion mapToPromotion(PromotionContract.CreateReq dto) {
        Promotion promotion = new Promotion();
        promotion.setTitle(dto.title());
        promotion.setDescription(dto.description());
        promotion.setDiscountType(dto.discountType());
        promotion.setPromotionType(dto.promotionType());
        promotion.setCondition(dto.condition());
        promotion.setDiscountValue(dto.discountValue());
        promotion.setMinOrderValue(dto.minOrderValue());
        promotion.setStartDate(dto.startDate());
        promotion.setEndDate(dto.endDate());
        promotion.setHidden(false);
        return promotion;
    }

    private List<PromotionDetail> mapToDetail(Promotion entity, List<PromotionContract.CreateDetailReq> dtoList) {
        List<PromotionDetail> details = new ArrayList<>();
        for (PromotionContract.CreateDetailReq dto : dtoList) {
            PromotionDetail detail = new PromotionDetail();
            detail.setPromotion(entity);
            detail.setProductId(dto.productId());
            detail.setMinQuantity(dto.minQuantity());
            details.add(detail);
        }
        return details;
    }

    private PromotionContract.Res mapToDTO(Promotion entity) {
        List<PromotionContract.DetailRes> details = entity.getDetails().stream().map(detail -> new PromotionContract.DetailRes(
                detail.getPromotionDetailId(),
                detail.getProductId(),
                detail.getMinQuantity()
        )).toList();
        return new PromotionContract.Res(
                entity.getPromotionId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDiscountType(),
                entity.getPromotionType(),
                entity.getCondition(),
                entity.getDiscountValue(),
                entity.getMinOrderValue(),
                entity.getStartDate(),
                entity.getEndDate(),
                details
        );
    }
}
