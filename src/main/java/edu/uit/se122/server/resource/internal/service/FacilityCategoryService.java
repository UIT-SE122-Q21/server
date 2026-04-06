package edu.uit.se122.server.resource.internal.service;

import edu.uit.se122.server.resource.FacilityCategoryContract;
import edu.uit.se122.server.resource.internal.entity.FacilityCategory;
import edu.uit.se122.server.resource.internal.repository.FacilityCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FacilityCategoryService {
    private final FacilityCategoryRepository facilityCategoryRepository;

    public List<FacilityCategoryContract.Response> getAll() {
        return facilityCategoryRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public FacilityCategoryContract.Response getById(Integer id) {
        return facilityCategoryRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public FacilityCategoryContract.Response create(FacilityCategoryContract.Request dto) {
        FacilityCategory category = new FacilityCategory();
        updateEntity(category, dto);
        FacilityCategory saved = facilityCategoryRepository.save(category);

        return mapToDTO(saved);
    }

    public FacilityCategoryContract.Response update(Integer id, FacilityCategoryContract.Request dto) {
        FacilityCategory category = facilityCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        updateEntity(category, dto);
        facilityCategoryRepository.save(category);
        return facilityCategoryRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void delete(Integer id) { facilityCategoryRepository.deleteById(id); }

    private void updateEntity(FacilityCategory entity, FacilityCategoryContract.Request dto) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
    }

    private FacilityCategoryContract.Response mapToDTO(FacilityCategory entity) {
        String formattedCategoryId = String.format("%03d", entity.getFacilityCategoryId());
        return new FacilityCategoryContract.Response(
                formattedCategoryId,
                entity.getName(),
                entity.getDescription()
        );
    }
}
