package edu.uit.se122.server.resource.internal.service;

import edu.uit.se122.server.common.enums.FacilityStatus;
import edu.uit.se122.server.resource.FacilityContract;
import edu.uit.se122.server.resource.internal.entity.Facility;
import edu.uit.se122.server.resource.internal.entity.FacilityCategory;
import edu.uit.se122.server.resource.internal.repository.FacilityCategoryRepository;
import edu.uit.se122.server.resource.internal.repository.FacilityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final FacilityCategoryRepository facilityCategoryRepository;

    public List<FacilityContract.Response> getAll() {
        return facilityRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public FacilityContract.Response getById(Integer id) {
        return facilityRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Facility not found"));
    }

    public FacilityContract.Response create(FacilityContract.CreateRequest dto) {
        FacilityCategory category = facilityCategoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Integer maxId = facilityRepository.findMaxProductIdByCategoryId(category.getFacilityCategoryId());
        int newFacilityId;
        if (maxId == null) {
            newFacilityId = category.getFacilityCategoryId() * 1000 + 1;
        } else {
            newFacilityId = maxId + 1;
        }

        Facility facility = new Facility();
        facility.setFacilityId(newFacilityId);
        facility.setStatus(FacilityStatus.Stock);
        createEntity(facility, dto);
        Facility saved = facilityRepository.save(facility);

        return mapToDTO(saved);
    }

    public FacilityContract.Response update(Integer id, FacilityContract.UpdateRequest dto) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facility not found"));
        updateEntity(facility, dto);
        facilityRepository.save(facility);
        return facilityRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Facility not found"));
    }

    public void delete(Integer id) { facilityRepository.deleteById(id); }

    private void createEntity(Facility entity, FacilityContract.CreateRequest dto) {
        entity.setName(dto.facilityName());
        entity.setDescription(dto.description());
        FacilityCategory category = facilityCategoryRepository.findById(dto.categoryId()).orElseThrow();
        entity.setCategory(category);
    }

    private void updateEntity(Facility entity, FacilityContract.UpdateRequest dto) {
        entity.setStatus(dto.status());
    }

    private FacilityContract.Response mapToDTO(Facility entity) {
        String formattedFacilityId = String.format("%06d", entity.getFacilityId());
        return new FacilityContract.Response(
                formattedFacilityId,
                entity.getName(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdateAt(),
                entity.getCategory().getName()
        );
    }
}
