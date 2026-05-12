package edu.uit.se122.server.booking.internal.service;

import edu.uit.se122.server.booking.ProductOrderContract;
import edu.uit.se122.server.booking.internal.entity.*;
import edu.uit.se122.server.booking.internal.repository.CourtOrderRepository;
import edu.uit.se122.server.booking.internal.repository.MemberCacheRepository;
import edu.uit.se122.server.booking.internal.repository.ProductCacheRepository;
import edu.uit.se122.server.booking.internal.repository.ProductOrderInvoiceRepository;
import edu.uit.se122.server.inventory.ProductApi;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductOrderService {
    private final ProductOrderInvoiceRepository invoiceRepository;
    private final CourtOrderRepository courtOrderRepository;
    private final ProductCacheRepository productCacheRepository;
    private final MemberCacheRepository memberCacheRepository;
    private final ProductApi productApi;

    public List<ProductOrderContract.OrderHistoryRes> getOrderHistory() {
        return courtOrderRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public void createOrderByCustomer(ProductOrderContract.CreateOrderByCustomer dto) {
        CourtOrder courtOrder = courtOrderRepository.findById(dto.courtOrderId())
                .orElseThrow(() -> new RuntimeException("Court order not found"));
        List<ProductOrderDetail> details = dto.detailReqs().stream().map(detailReq -> {
            ProductOrderDetail detail = new ProductOrderDetail();
            detail.setProductId(detailReq.productDetailId());
            detail.setQuantity(detailReq.quantity());
            detail.setDraft(true);
            detail.setCourtOrder(courtOrder);
            return detail;
        }).collect(Collectors.toCollection(ArrayList::new));
        courtOrder.setProductOrderDetails(details);
        courtOrderRepository.save(courtOrder);
    }

    public ProductOrderContract.CalculateInvoiceRes calculateInvoice(ProductOrderContract.InvoiceReq dto) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal changeAmount;

        for (ProductOrderContract.DetailReq detail : dto.detailReqs()) {
            ProductCache productCache = productCacheRepository.findById(detail.productDetailId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            totalAmount = totalAmount.add(productCache.getUnitPrice().multiply(BigDecimal.valueOf(detail.quantity())));
        }
        changeAmount = dto.givenAmount().subtract(totalAmount);
        return new ProductOrderContract.CalculateInvoiceRes(totalAmount, changeAmount);
    }

    public void createInvoice(ProductOrderContract.InvoiceReq dto) {
        ProductOrderContract.CalculateInvoiceRes calculateResult = calculateInvoice(dto);

        CourtOrder courtOrder = courtOrderRepository.findById(dto.courtOrderId())
                .orElseThrow(() -> new RuntimeException("Court order not found"));
        updateEntity(courtOrder, dto);
        productApi.DecreaseQuantity(dto.detailReqs());

        ProductOrderInvoice invoice = new ProductOrderInvoice();
        invoice.setTotalAmount(calculateResult.totalAmount());
        invoice.setGivenAmount(dto.givenAmount());
        invoice.setChangeAmount(calculateResult.changeAmount());
        invoice.setCourtOrder(courtOrder);
        invoiceRepository.save(invoice);
    }

    private void updateEntity(CourtOrder entity, ProductOrderContract.InvoiceReq dto) {
        List<ProductOrderDetail> details = dto.detailReqs().stream().map(detailReq -> {
            ProductOrderDetail detail = new ProductOrderDetail();
            detail.setProductId(detailReq.productDetailId());
            detail.setQuantity(detailReq.quantity());
            detail.setDraft(false);
            detail.setCourtOrder(entity);
            return detail;
        }).collect(Collectors.toCollection(ArrayList::new));
        entity.setProductOrderDetails(details);
    }

    private ProductOrderContract.OrderHistoryRes mapToDTO(CourtOrder entity) {
        String customerName;
        if (entity.getGuest()) {
            customerName = entity.getGuestName();
        } else {
            MemberCache memberCache = memberCacheRepository.findById(entity.getMemberId())
                    .orElseThrow(() -> new RuntimeException("Member not found"));
            customerName = memberCache.getName();
        }

        return new ProductOrderContract.OrderHistoryRes(
                entity.getOrderDate(),
                customerName,
                entity.getProductOrderDetails().stream().map(d -> new ProductOrderContract.OrderHistoryDetailRes(
                        productCacheRepository.findById(d.getProductId()).orElseThrow(() -> new RuntimeException("Product not found")).getProductId(),
                        productCacheRepository.findById(d.getProductId()).orElseThrow(() -> new RuntimeException("Product not found")).getName(),
                        d.getQuantity()
                )).toList()
        );
    }
}
