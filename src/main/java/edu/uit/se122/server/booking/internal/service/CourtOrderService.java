package edu.uit.se122.server.booking.internal.service;

import edu.uit.se122.server.booking.CourtOrderContract;
import edu.uit.se122.server.booking.internal.entity.CourtOrder;
import edu.uit.se122.server.booking.internal.entity.CourtOrderDetail;
import edu.uit.se122.server.booking.internal.repository.CourtOrderDetailRepository;
import edu.uit.se122.server.booking.internal.repository.CourtOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourtOrderService {
    private final CourtOrderRepository courtOrderRepository;
    private final CourtOrderDetailRepository detailRepository;

    private void updateEntity(CourtOrder entity, CourtOrderContract.Request dto) {
        entity.setOrderDate(dto.orderDate());
        entity.setStatus(dto.status());
        entity.setAdminId(dto.adminId());
        entity.setUserId(dto.userId());
        entity.setEmail(dto.email());
        List<CourtOrderDetail> details = dto.detailRequests().stream().map(detailRequest -> {
            CourtOrderDetail detail = new CourtOrderDetail();
            detail.setCourtId(detailRequest.courtId());
            detail.setFromTime(detailRequest.fromTime());
            detail.setToTime(detailRequest.toTime());
            return detail;
        }).toList();
        entity.setCourtOrderDetails(details);
    }
}
