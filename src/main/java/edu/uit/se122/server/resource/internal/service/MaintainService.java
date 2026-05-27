package edu.uit.se122.server.resource.internal.service;

import edu.uit.se122.server.common.enums.MaintainStatus;
import edu.uit.se122.server.resource.MaintainContract;
import edu.uit.se122.server.resource.internal.entity.Court;
import edu.uit.se122.server.resource.internal.entity.Facility;
import edu.uit.se122.server.resource.internal.entity.FacilityCategory;
import edu.uit.se122.server.resource.internal.entity.Maintain;
import edu.uit.se122.server.resource.internal.repository.CourtRepository;
import edu.uit.se122.server.resource.internal.repository.FacilityCategoryRepository;
import edu.uit.se122.server.resource.internal.repository.FacilityRepository;
import edu.uit.se122.server.resource.internal.repository.MaintainRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintainService {
    private final MaintainRepository maintainRepository;
    private final FacilityCategoryRepository categoryRepository;
    private final FacilityRepository facilityRepository;
    private final CourtRepository courtRepository;

    public List<MaintainContract.Res> getAll() {
        return maintainRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public MaintainContract.Res getById(Integer id) {
        return maintainRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Maintain not found"));
    }

    public void create(MaintainContract.Req dto) {
        Maintain maintain = new Maintain();
        maintain.setStatus(MaintainStatus.Pending);
        updateEntity(maintain, dto);
        maintainRepository.save(maintain);
    }

    public void update(Integer id, MaintainContract.Req dto) {
        Maintain maintain = maintainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintain not found"));
        updateEntity(maintain, dto);
        maintainRepository.save(maintain);
    }

    public void delete(Integer id) { maintainRepository.deleteById(id); }

    private void updateEntity(Maintain entity, MaintainContract.Req dto) {
        FacilityCategory category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Facility facility = Optional.ofNullable(dto.facilityId())
                .filter(id -> id > 0)
                .flatMap(facilityRepository::findById)
                .orElse(null);
        Court court = Optional.ofNullable(dto.courtId())
                        .filter(id -> id > 0)
                                .flatMap(courtRepository::findById)
                                        .orElse(null);
        entity.setDetail(dto.detail());
        entity.setCategory(category);
        entity.setFacility(facility);
        entity.setCourt(court);
    }

    private MaintainContract.Res mapToDTO(Maintain entity) {
        return new MaintainContract.Res(
                entity.getMaintainId(),
                entity.getDetail(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCategory().getName(),
                Optional.ofNullable(entity.getFacility()).map(Facility::getName).orElse(null),
                Optional.ofNullable(entity.getCourt()).map(Court::getNumOfIndex).orElse(null)
        );
    }
}
