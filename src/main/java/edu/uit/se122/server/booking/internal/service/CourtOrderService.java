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

    public List<CourtOrderContract.Response> getAll() {
        return courtOrderRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public CourtOrderContract.Response getById(Integer id) {
        return courtOrderRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void createByAdmin(Integer adminId, CourtOrderContract.Request dto) {
        CourtOrder order = new CourtOrder();
        updateEntity(order, dto);
        order.setAdminId(adminId);
        courtOrderRepository.save(order);
    }

    public void createByUser(Integer userId, CourtOrderContract.Request dto) {
        CourtOrder order = new CourtOrder();
        updateEntity(order, dto);
        if (userId != null) {
            order.setUserId(userId);
            order.setGuest(false);
        } else {
            order.setGuest(true);
            order.setGuestName(dto.guestName());
            order.setGuestEmail(dto.guestEmail());
            order.setGuestPhoneNumber(dto.guestPhoneNumber());
        }
        courtOrderRepository.save(order);
    }

    private void updateEntity(CourtOrder entity, CourtOrderContract.Request dto) {
        entity.setOrderDate(dto.orderDate());
        entity.setStatus(dto.status());
        entity.setAdminId(dto.adminId());
        entity.setUserId(dto.userId());
        List<CourtOrderDetail> details = dto.detailRequests().stream().map(detailRequest -> {
            CourtOrderDetail detail = new CourtOrderDetail();
            detail.setCourtId(detailRequest.courtId());
            detail.setFromTime(detailRequest.fromTime());
            detail.setToTime(detailRequest.toTime());
            detail.setCourtOrder(entity);
            return detail;
        }).toList();
        entity.setCourtOrderDetails(details);
    }

    private CourtOrderContract.Response mapToDTO(CourtOrder entity) {
        return new CourtOrderContract.Response(
                entity.getCourtOrderId(),
                entity.getStatus(),
                entity.getAdminId(),
                entity.getUserId(),
                entity.getGuest(),
                entity.getGuestName(),
                entity.getGuestEmail(),
                entity.getGuestPhoneNumber(),
                entity.getCourtOrderDetails().stream().map(d -> new CourtOrderContract.DetailResponse(
                        d.getCourtOrderDetailId(),
                        d.getCourtId(),
                        d.getFromTime(),
                        d.getToTime()
                )).toList()
        );
    }
}
