package edu.uit.se122.server.resource.internal.service;

import edu.uit.se122.server.resource.CourtContract;
import edu.uit.se122.server.resource.internal.component.CourtPrice;
import edu.uit.se122.server.resource.internal.component.PriceManager;
import edu.uit.se122.server.resource.internal.entity.Court;
import edu.uit.se122.server.resource.internal.repository.CourtRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourtService {
    private final CourtRepository courtRepository;
    private final PriceManager priceManager;
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT_LOGGER");

    public List<CourtContract.Response> getAll() {
        return courtRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public CourtContract.Response getById(Integer id) {
        return courtRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Court not found"));
    }

    public void create(CourtContract.Request dto) {
        Court court = new Court();
        updateEntity(court, dto);
        courtRepository.save(court);
    }

    public void update(Integer id, CourtContract.Request dto) {
        Court court = courtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Court not found"));
        updateEntity(court, dto);
        courtRepository.save(court);
    }

    public void updateGlobalCourtPrice(Integer adminId, CourtContract.UpdateCourtPriceReq dto) {
        if (dto.newPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        CourtPrice oldConfig = priceManager.getCurrentCourtPrice();
        BigDecimal oldCourtPrice = oldConfig.getUnitPrice();
        CourtPrice newCourtPrice = new CourtPrice(dto.newPrice());
        priceManager.updateCourtPrice(newCourtPrice);

        auditLogger.info("{\"event_type\":\"SYSTEM_CONFIG_CHANGE\",\"actor\":\"{}\",\"ip\":\"{}\",\"target\":\"COURT_UNIT_PRICE\",\"old_value\":\"{}\",\"new_value\":\"{}\",\"status\":\"SUCCESS\"}",
                adminId, null, oldCourtPrice, dto.newPrice());
    }

    public void delete(Integer id) { courtRepository.deleteById(id); }

    private void updateEntity(Court entity, CourtContract.Request dto) {
        entity.setNumOfIndex(dto.numOfIndex());
        entity.setMaintenance(false);
    }

    private CourtContract.Response mapToDTO(Court entity) {
        return new CourtContract.Response(
                entity.getCourtId(),
                entity.getNumOfIndex(),
                entity.getMaintenance(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
