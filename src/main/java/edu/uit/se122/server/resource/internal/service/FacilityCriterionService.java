package edu.uit.se122.server.resource.internal.service;

import edu.uit.se122.server.resource.FacilityCriterionContract;
import edu.uit.se122.server.resource.internal.entity.FacilityCategory;
import edu.uit.se122.server.resource.internal.entity.FacilityCriterion;
import edu.uit.se122.server.resource.internal.repository.FacilityCategoryRepository;
import edu.uit.se122.server.resource.internal.repository.FacilityCriterionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FacilityCriterionService {
    private final FacilityCriterionRepository facilityCriterionRepository;
    private final FacilityCategoryRepository facilityCategoryRepository;

    public List<FacilityCriterionContract.Response> getAll() {
        return facilityCriterionRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public FacilityCriterionContract.Response getById(Integer id) {
        return facilityCriterionRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Criterion not found"));
    }

    public void create(FacilityCriterionContract.Request dto) {
        FacilityCategory category = facilityCategoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        FacilityCriterion criterion = new FacilityCriterion();
        updateEntity(criterion, dto);
        FacilityCriterion saved = facilityCriterionRepository.save(criterion);
    }

    public void update(Integer id, FacilityCriterionContract.Request dto) {
        FacilityCriterion criterion = facilityCriterionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Criterion not found"));
        updateEntity(criterion, dto);
        facilityCriterionRepository.save(criterion);
    }

    public void delete(Integer id) { facilityCriterionRepository.deleteById(id); }

    private void updateEntity(FacilityCriterion entity, FacilityCriterionContract.Request dto) {
        entity.setDetail(dto.detail());
        entity.setSchedule(dto.schedule());
        FacilityCategory category = facilityCategoryRepository.findById(dto.categoryId()).orElseThrow();
        entity.setCategory(category);
    }

    private FacilityCriterionContract.Response mapToDTO(FacilityCriterion entity) {
        return new FacilityCriterionContract.Response(
                entity.getCriterionId(),
                entity.getDetail(),
                entity.getSchedule(),
                entity.getCategory().getName()
        );
    }
}
