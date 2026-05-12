package edu.uit.se122.server.booking.internal.service;

import edu.uit.se122.server.booking.CourtOrderContract;
import edu.uit.se122.server.booking.internal.entity.CourtCache;
import edu.uit.se122.server.booking.internal.entity.CourtOrder;
import edu.uit.se122.server.booking.internal.entity.CourtOrderDetail;
import edu.uit.se122.server.booking.internal.entity.CourtOrderInvoice;
import edu.uit.se122.server.booking.internal.repository.CourtCacheRepository;
import edu.uit.se122.server.booking.internal.repository.CourtOrderInvoiceRepository;
import edu.uit.se122.server.booking.internal.repository.CourtOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourtOrderService {
    private final CourtOrderRepository courtOrderRepository;
    private final CourtOrderInvoiceRepository invoiceRepository;
    private final CourtCacheRepository courtCacheRepository;

    public List<CourtOrderContract.Res> getAll() {
        return courtOrderRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public CourtOrderContract.Res getById(Integer id) {
        return courtOrderRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void createByAdmin(Integer adminId, CourtOrderContract.CreateByAdminReq dto) {
        CourtOrder order = new CourtOrder();
        order.setOrderDate(dto.orderDate());
        order.setStartHour(dto.startHour());
        order.setEndHour(dto.endHour());
        order.setAdminId(adminId);
        List<CourtOrderDetail> detail = mapToDetails(order, dto.courtIds());
        order.setCourtOrderDetails(detail);
        CourtOrder saved = courtOrderRepository.save(order);

        calculateInvoice(saved);
    }

    public void createByUser(Integer memberId, CourtOrderContract.CreateByCustomerReq dto) {
        CourtOrder order = new CourtOrder();
        order.setOrderDate(dto.orderDate());
        order.setStartHour(dto.startHour());
        order.setEndHour(dto.endHour());
        if (memberId != null) {
            order.setMemberId(memberId);
            order.setGuest(false);
        } else {
            order.setGuest(true);
            order.setGuestName(dto.guestName());
            order.setGuestEmail(dto.guestEmail());
            order.setGuestPhoneNumber(dto.guestPhoneNumber());
        }
        List<CourtOrderDetail> details = mapToDetails(order, dto.courtIds());
        order.setCourtOrderDetails(details);
        courtOrderRepository.save(order);
    }

    public void calculateInvoice(CourtOrder courtOrder) {
        double totalAmount = 0;
        double totalTime = 0;
        int totalCourts = courtOrder.getCourtOrderDetails().size();

        for (CourtOrderDetail detail : courtOrder.getCourtOrderDetails()) {
            CourtCache courtCache = courtCacheRepository.findById(detail.getCourtId())
                    .orElseThrow(() -> new RuntimeException("Court not found"));

            Duration duration = Duration.between(detail.getFromTime(), detail.getToTime());
            double hours = duration.toMinutes() / 60.0;
            double amount = courtCache.getUnitPrice() * hours;

            totalTime += hours;
            totalAmount += amount;
        }

        CourtOrderInvoice invoice = new CourtOrderInvoice();
        invoice.setQuantity(totalCourts);
        invoice.setTotalTime(totalTime);
        invoice.setTotalAmount(totalAmount);
        invoice.setCourtOrder(courtOrder);
        invoiceRepository.save(invoice);
    }

    private List<CourtOrderDetail> mapToDetails(CourtOrder entity, List<Integer> courtIds) {
        return courtIds.stream().map(courtId -> {
            CourtOrderDetail detail = new CourtOrderDetail();
            detail.setCourtId(courtId);
            detail.setCourtOrder(entity);
            return detail;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private CourtOrderContract.Res mapToDTO(CourtOrder entity) {
        return new CourtOrderContract.Res(
                entity.getCourtOrderId(),
                entity.getOrderDate(),
                entity.getStartHour(),
                entity.getEndHour(),
                entity.getStatus(),
                entity.getAdminId(),
                entity.getMemberId(),
                entity.getGuest(),
                entity.getGuestName(),
                entity.getGuestEmail(),
                entity.getGuestPhoneNumber(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCourtOrderDetails().stream().map(CourtOrderDetail::getCourtId).toList()
        );
    }
}
