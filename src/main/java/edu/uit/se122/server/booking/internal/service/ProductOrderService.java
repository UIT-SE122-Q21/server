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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
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
        List<ProductOrderDetail> details = dto.details().stream().map(detailReq -> {
            ProductOrderDetail detail = new ProductOrderDetail();
            detail.setProductId(detailReq.productId());
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
        List<String> unavailableRacketNameList = new ArrayList<>();
        List<String> unavailableProductNameList = new ArrayList<>();
        CourtOrder courtOrder = courtOrderRepository.findById(dto.courtOrderId())
                .orElseThrow(() -> new RuntimeException("Court order not found"));

        for (ProductOrderContract.DetailReq detail : dto.details()) {
            ProductCache productCache = productCacheRepository.findById(detail.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (detail.productCategoryId() == 1) {
                Integer racketQuantity = productApi.getQuantity(detail.productId());
                Integer unavailableRacket = courtOrderRepository.countUnavailableRacket(
                        detail.productId(),
                        courtOrder.getOrderDate(),
                        courtOrder.getStartHour(),
                        courtOrder.getEndHour()
                );
                log.info("Racket quantity: {}, unavailable racket: {}", racketQuantity, unavailableRacket);
                if (racketQuantity - unavailableRacket < detail.quantity()) {
                    unavailableRacketNameList.add(productCache.getName());
                }
            } else {
                Integer productQuantity = productApi.getQuantity(detail.productId());
                Integer orderedProduct = courtOrderRepository.countOrderedProduct(detail.productId(), courtOrder.getOrderDate());
                if (productQuantity - orderedProduct < detail.quantity()) {
                    unavailableProductNameList.add(productCache.getName());
                }
            }
            totalAmount = totalAmount.add(productCache.getUnitPrice().multiply(BigDecimal.valueOf(detail.quantity())));
        }
        if (!unavailableRacketNameList.isEmpty()) {
            throw new RuntimeException("Racket " + String.join(", ", unavailableRacketNameList) + " is unavailable");
        } else if (!unavailableProductNameList.isEmpty()) {
            throw new RuntimeException("Product " + String.join(", ", unavailableProductNameList) + " is unavailable");
        }
        changeAmount = dto.givenAmount().subtract(totalAmount);
        return new ProductOrderContract.CalculateInvoiceRes(totalAmount, changeAmount);
    }

    public void createInvoice(ProductOrderContract.InvoiceReq dto) {
        ProductOrderContract.CalculateInvoiceRes calculateResult = calculateInvoice(dto);

        CourtOrder courtOrder = courtOrderRepository.findById(dto.courtOrderId())
                .orElseThrow(() -> new RuntimeException("Court order not found"));
        updateEntity(courtOrder, dto);
        productApi.decreaseQuantity(dto.details());

        ProductOrderInvoice invoice = new ProductOrderInvoice();
        invoice.setTotalAmount(calculateResult.totalAmount());
        invoice.setGivenAmount(dto.givenAmount());
        invoice.setChangeAmount(calculateResult.changeAmount());
        invoice.setCourtOrder(courtOrder);
        invoiceRepository.save(invoice);
    }

    private void updateEntity(CourtOrder entity, ProductOrderContract.InvoiceReq dto) {
        List<ProductOrderDetail> details = dto.details().stream().map(detailReq -> {
            ProductOrderDetail detail = new ProductOrderDetail();
            detail.setProductId(detailReq.productId());
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
