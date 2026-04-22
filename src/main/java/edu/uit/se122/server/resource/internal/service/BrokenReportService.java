package edu.uit.se122.server.resource.internal.service;

import edu.uit.se122.server.common.enums.MaintainStatus;
import edu.uit.se122.server.resource.BrokenReportContract;
import edu.uit.se122.server.resource.internal.entity.BrokenReport;
import edu.uit.se122.server.resource.internal.entity.FacilityCategory;
import edu.uit.se122.server.resource.internal.repository.BrokenReportRepository;
import edu.uit.se122.server.resource.internal.repository.FacilityCategoryRepository;
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
    private final FacilityCategoryRepository categoryRepository;

    public List<BrokenReportContract.Response> getAll() {
        return brokenReportRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public BrokenReportContract.Response getById(Integer id) {
        return brokenReportRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    public void create(BrokenReportContract.Request dto) throws IOException {
        BrokenReport report = new BrokenReport();
        updateEntity(report, dto);
        BrokenReport saved = brokenReportRepository.save(report);
    }

    public void update(Integer id, BrokenReportContract.Request dto) throws IOException {
        BrokenReport report = brokenReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        updateEntity(report, dto);
        brokenReportRepository.save(report);
    }

    public void delete(Integer id) {
        brokenReportRepository.deleteById(id);
    }

    private void updateEntity(BrokenReport entity, BrokenReportContract.Request dto) {
        entity.setStatus(MaintainStatus.Pending);
        entity.setContent(dto.content());
        entity.setAttachment(dto.attachment());
        FacilityCategory category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        entity.setCategory(category);
    }

    private BrokenReportContract.Response mapToDTO(BrokenReport entity) {
        return new BrokenReportContract.Response(
            entity.getBrokenReportId(),
            entity.getCategory().getName(),
            entity.getStatus(),
            entity.getContent(),
            entity.getAttachment(),
            entity.getFeedback(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
