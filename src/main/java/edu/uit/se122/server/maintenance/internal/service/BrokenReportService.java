package edu.uit.se122.server.maintenance.internal.service;

import edu.uit.se122.server.maintenance.internal.dto.BrokenReportReqDTO;
import edu.uit.se122.server.maintenance.internal.dto.BrokenReportResDTO;
import edu.uit.se122.server.maintenance.internal.entity.BrokenReport;
import edu.uit.se122.server.maintenance.internal.repository.BrokenReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BrokenReportService {
    private final BrokenReportRepository brokenReportRepository;

    public List<BrokenReportResDTO> getAll() {
        return brokenReportRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public BrokenReportResDTO getById(Integer id) {
        return brokenReportRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    public BrokenReportResDTO create(BrokenReportReqDTO dto) throws IOException {
        BrokenReport report = new BrokenReport();
        updateEntity(report, dto);
        BrokenReport saved = brokenReportRepository.save(report);

        return mapToDTO(saved);
    }

    public BrokenReportResDTO update(Integer id, BrokenReportReqDTO dto) throws IOException {
        BrokenReport report = brokenReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        updateEntity(report, dto);
        brokenReportRepository.save(report);
        return brokenReportRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    public void delete(Integer id) {
        brokenReportRepository.deleteById(id);
    }

    private void updateEntity(BrokenReport entity, BrokenReportReqDTO dto) {
        entity.setStatus(dto.getStatus());
        entity.setContent(dto.getContent());
        entity.setAttachment(dto.getAttachment());
    }

    private BrokenReportResDTO mapToDTO(BrokenReport entity) {
        BrokenReportResDTO dto = new BrokenReportResDTO();
        dto.setBrokenReportId(entity.getBrokenReportId());
        dto.setGuest(entity.getGuest());
        dto.setStatus(entity.getStatus());
        dto.setContent(entity.getContent());
        dto.setAttachment(entity.getAttachment());
        dto.setFeedback(entity.getFeedback());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}
