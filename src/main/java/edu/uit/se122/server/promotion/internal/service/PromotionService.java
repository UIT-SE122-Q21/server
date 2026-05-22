package edu.uit.se122.server.promotion.internal.service;

import edu.uit.se122.server.promotion.PromotionContract;
import edu.uit.se122.server.promotion.internal.entity.Promotion;
import edu.uit.se122.server.promotion.internal.entity.PromotionDetail;
import edu.uit.se122.server.promotion.internal.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PromotionService {
    private final PromotionRepository promotionRepository;

    // CREATE
    public void create(PromotionContract.CreateReq dto) {
        Promotion promotion = mapToPromotion(dto);
        List<PromotionDetail> detail = mapToDetail(dto.details());
        for (PromotionDetail d : detail) {
            promotion.addDetail(d);
        }
        promotionRepository.save(promotion);
    }

    public List<PromotionContract.ResByAdmin> getAllByAdmin() {
        return promotionRepository.findAll().stream().map(this::mapToResByAdmin).toList();
    }

    public PromotionContract.ResByAdmin getByIdByAdmin(Integer id) {
        return promotionRepository.findById(id).map(this::mapToResByAdmin)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    public List<PromotionContract.ResByOperator> getAllByOperator() {
        return promotionRepository.findAll().stream().map(this::mapToResByOperator).toList();
    }

    public PromotionContract.ResByOperator getByIdByOperator(Integer id) {
        return promotionRepository.findById(id).map(this::mapToResByOperator)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    public void hide(Integer id, PromotionContract.HideReq dto) {
        promotionRepository.findById(id).ifPresent(promotion -> promotion.setHidden(dto.hidden()));
    }

    private Promotion mapToPromotion(PromotionContract.CreateReq dto) {
        return Promotion.builder()
                .title(dto.title())
                .description(dto.description())
                .condition(dto.condition())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .hidden(false)
                .build();
    }

    private List<PromotionDetail> mapToDetail(List<PromotionContract.CreateDetailReq> dtoList) {
        List<PromotionDetail> details = new ArrayList<>();
        if (dtoList != null) {
            for (PromotionContract.CreateDetailReq dto : dtoList) {
                PromotionDetail detail = new PromotionDetail();
                detail.setProductId(dto.productId());
                detail.setMinQuantity(dto.minQuantity());
                details.add(detail);
            }
        }
        return details;
    }

    private PromotionContract.ResByAdmin mapToResByAdmin(Promotion entity) {
        List<PromotionContract.DetailRes> details = entity.getDetails().stream().map(detail -> new PromotionContract.DetailRes(
                detail.getPromotionDetailId(),
                detail.getProductId(),
                detail.getMinQuantity()
        )).toList();
        return new PromotionContract.ResByAdmin(
                entity.getPromotionId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getHidden(),
                details
        );
    }

    private PromotionContract.ResByOperator mapToResByOperator(Promotion entity) {
        List<PromotionContract.DetailRes> details = entity.getDetails().stream().map(detail -> new PromotionContract.DetailRes(
                detail.getPromotionDetailId(),
                detail.getProductId(),
                detail.getMinQuantity()
        )).toList();
        return new PromotionContract.ResByOperator(
                entity.getPromotionId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCondition(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getHidden(),
                details
        );
    }
}
