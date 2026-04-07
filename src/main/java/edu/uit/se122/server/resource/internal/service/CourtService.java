package edu.uit.se122.server.resource.internal.service;

import edu.uit.se122.server.resource.CourtContract;
import edu.uit.se122.server.resource.internal.entity.Court;
import edu.uit.se122.server.resource.internal.repository.CourtRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourtService {
    private final CourtRepository courtRepository;

    public List<CourtContract.Response> getAll() {
        return courtRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public CourtContract.Response getById(Integer id) {
        return courtRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Court not found"));
    }

    public CourtContract.Response create(CourtContract.Request dto) {
        Court court = new Court();
        updateEntity(court, dto);
        Court saved = courtRepository.save(court);

        return mapToDTO(saved);
    }

    public CourtContract.Response update(Integer id, CourtContract.Request dto) {
        Court court = courtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Court not found"));
        updateEntity(court, dto);
        courtRepository.save(court);
        return courtRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Court not found"));
    }

    public void delete(Integer id) { courtRepository.deleteById(id); }

    private void updateEntity(Court entity, CourtContract.Request dto) {
        entity.setName(dto.name());
        entity.setNumOfIndex(dto.numOfIndex());
        entity.setUnitPrice(dto.unitPrice());
        entity.setStatus(dto.status());
    }

    private CourtContract.Response mapToDTO(Court entity) {
        return new CourtContract.Response(
                entity.getCourtId(),
                entity.getName(),
                entity.getNumOfIndex(),
                entity.getUnitPrice(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
