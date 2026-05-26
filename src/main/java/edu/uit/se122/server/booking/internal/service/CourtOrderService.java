package edu.uit.se122.server.booking.internal.service;

import edu.uit.se122.server.booking.OrderContract;
import edu.uit.se122.server.booking.internal.entity.*;
import edu.uit.se122.server.booking.internal.repository.*;
import edu.uit.se122.server.common.enums.OrderStatus;
import edu.uit.se122.server.inventory.ProductApi;
import edu.uit.se122.server.promotion.PromotionApi;
import edu.uit.se122.server.promotion.PromotionContract;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CourtOrderService {
    private final CourtOrderRepository courtOrderRepository;
    private final OrderInvoiceRepository invoiceRepository;
    private final CourtCacheRepository courtCacheRepository;
    private final ProductCacheRepository productCacheRepository;
    private final MemberCacheRepository memberCacheRepository;
    private final ProductApi productApi;
    private final PromotionApi promotionApi;
    private final ApplicationEventPublisher eventPublisher;

    public List<OrderContract.ResByAdmin> getAllByAdmin() {
        return courtOrderRepository.findAll().stream().map(this::mapToResponseByAdmin).toList();
    }

    public List<OrderContract.ResByCustomer> getAllByCustomer(Integer memberId) {
        return courtOrderRepository.findByMemberId(memberId).stream().map(this::mapToResponseByCustomer).toList();
    }

    public OrderContract.ResByAdmin getById(Integer id) {
        return courtOrderRepository.findById(id).map(this::mapToResponseByAdmin)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public OrderContract.CreatedOrderRes createOrderByAdmin(Integer adminId, OrderContract.CreateOrderByAdminReq dto) {
        CourtOrder order = CourtOrder.builder()
                .orderDate(dto.orderDate())
                .startHour(dto.startHour())
                .endHour(dto.endHour())
                .adminId(adminId)
                .build();
        return getCreatedOrderRes(order, dto.courtIds(), dto.productDetails());
    }

    public OrderContract.CreatedOrderRes createOrderByCustomer(Integer memberId, OrderContract.CreateOrderByCustomerReq dto) {
        CourtOrder order;
        if (memberId == null) {
            order = CourtOrder.builder()
                    .orderDate(dto.orderDate())
                    .startHour(dto.startHour())
                    .endHour(dto.endHour())
                    .guestName(dto.guestName())
                    .guestEmail(dto.guestEmail())
                    .guestPhoneNumber(dto.guestPhoneNumber())
                    .build();
        } else {
            order = CourtOrder.builder()
                    .orderDate(dto.orderDate())
                    .startHour(dto.startHour())
                    .endHour(dto.endHour())
                    .memberId(memberId)
                    .build();
        }
        return getCreatedOrderRes(order, dto.courtIds(), dto.productDetails());
    }

    public void updateProductOrderDetails(Integer courtOrderId, List<OrderContract.ProductOrderDetailReq> dtoList) {
        CourtOrder courtOrder = courtOrderRepository.findById(courtOrderId)
                .orElseThrow(() -> new RuntimeException("Court order not found"));
        if (dtoList == null) {
            courtOrder.getProductOrderDetails().clear();
            return;
        }
        checkOrderedProduct(courtOrder, dtoList);

        Map<Integer, ProductOrderDetail> existingDetailsMap = courtOrder.getProductOrderDetails().stream()
                .collect(Collectors.toMap(ProductOrderDetail::getProductId, d -> d));
        for (OrderContract.ProductOrderDetailReq dto : dtoList) {
            ProductOrderDetail detail;
            if (dto.productId() != null && existingDetailsMap.containsKey(dto.productId())) {
                detail = existingDetailsMap.get(dto.productId());
                detail.setQuantity(dto.quantity());
                existingDetailsMap.remove(dto.productId());
            } else {
                detail = mapToProductOrderDetail(dto);
                courtOrder.addProductOrderDetail(detail);
            }
        }
        if (!existingDetailsMap.isEmpty()) {
            courtOrder.getProductOrderDetails().removeAll(existingDetailsMap.values());
        }
        courtOrderRepository.save(courtOrder);
    }

    public OrderContract.CalculateDepositRes calculateDepositValue(Integer id) {
        CourtOrder courtOrder = courtOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Court order not found"));

        BigDecimal courtTotalAmount = calculateCourtTotalBeforeDiscount(courtOrder);
        BigDecimal depositAmount = courtTotalAmount.multiply(BigDecimal.valueOf(0.5));
        OrderInvoice invoice = new OrderInvoice();
        invoice.setDepositAmount(depositAmount);
        invoice.setCourtOrder(courtOrder);
        invoiceRepository.save(invoice);

        return new OrderContract.CalculateDepositRes(depositAmount);
    }

    public OrderContract.CalculateTotalRes calculateTotalValue(Integer id, OrderContract.CreateInvoiceByAdminReq dto) {
        CourtOrder courtOrder = courtOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Court order not found"));
        PromotionContract.OrderContext orderContext = new PromotionContract.OrderContext();

        BigDecimal courtTotalAmount = calculateCourtTotalBeforeDiscount(courtOrder);
        BigDecimal productTotalAmount = calculateProductTotalBeforeDiscount(courtOrder);
        BigDecimal totalBeforeDiscount = courtTotalAmount.add(productTotalAmount);
        orderContext.setTotalBeforeDiscount(totalBeforeDiscount);
        BigDecimal totalDiscount = promotionApi.applyPromotion(orderContext);
        BigDecimal totalAmount = totalBeforeDiscount.subtract(totalDiscount);
        BigDecimal changeAmount = dto.givenAmount().subtract(totalAmount);

        return new OrderContract.CalculateTotalRes(
                totalBeforeDiscount,
                totalDiscount,
                totalAmount,
                changeAmount,
                String.join(",", orderContext.getPromotionDescriptions())
        );
    }

    public void createInvoiceByCustomer(Integer id) {
        OrderContract.CalculateDepositRes calculateResult = calculateDepositValue(id);
        CourtOrder courtOrder = courtOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Court order not found"));
        courtOrder.setStatus(OrderStatus.Ordered);

        OrderInvoice invoice = new OrderInvoice();
        invoice.setDepositAmount(calculateResult.depositAmount());
        courtOrder.setOrderInvoice(invoice);
        invoiceRepository.save(invoice);
    }

    public void createInvoiceByAdmin(Integer id, OrderContract.CreateInvoiceByAdminReq dto) {
        OrderContract.CalculateTotalRes calculateResult = calculateTotalValue(id, dto);
        CourtOrder courtOrder = courtOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Court order not found"));
        List<OrderContract.ProductOrderDetailReq> details = new ArrayList<>();

        for (ProductOrderDetail detail : courtOrder.getProductOrderDetails()) {
            ProductCache productCache = productCacheRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            details.add(new OrderContract.ProductOrderDetailReq(productCache.getProductId(), productCache.getProductCategoryId(), detail.getQuantity()));
        }
        productApi.decreaseQuantity(details);

        courtOrder.setStatus(OrderStatus.Completed);
        OrderInvoice invoice = new OrderInvoice();
        invoice.setTotalAmount(calculateResult.totalAmount());
        invoice.setGivenAmount(dto.givenAmount());
        invoice.setChangeAmount(calculateResult.changeAmount());
        invoice.setPaymentMethod(dto.paymentMethod());
        courtOrder.setOrderInvoice(invoice);
        invoiceRepository.save(invoice);
    }

    private BigDecimal calculateCourtTotalBeforeDiscount(CourtOrder courtOrder) {
        BigDecimal courtTotalAmount = BigDecimal.ZERO;
        Duration duration = Duration.between(courtOrder.getStartHour(), courtOrder.getEndHour());

        for (CourtOrderDetail detail : courtOrder.getCourtOrderDetails()) {
            CourtCache courtCache = courtCacheRepository.findById(detail.getCourtId())
                    .orElseThrow(() -> new RuntimeException("Court not found"));

            double hours = duration.toMinutes() / 60.0;
            double amount = courtCache.getUnitPrice() * hours;

            courtTotalAmount = courtTotalAmount.add(BigDecimal.valueOf(amount));
        }
        return courtTotalAmount;
    }

    private void checkOrderedProduct(CourtOrder courtOrder, List<OrderContract.ProductOrderDetailReq> dtoList) {
        List<String> unavailableRacketNameList = new ArrayList<>();
        List<String> unavailableProductNameList = new ArrayList<>();

        for (OrderContract.ProductOrderDetailReq dto : dtoList) {
            ProductCache productCache = productCacheRepository.findById(dto.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (dto.productCategoryId() == 1) {
                Integer racketQuantity = productApi.getQuantity(dto.productId());
                Integer unavailableRacket = courtOrderRepository.countUnavailableRacket(
                        dto.productId(),
                        courtOrder.getOrderDate(),
                        courtOrder.getStartHour(),
                        courtOrder.getEndHour()
                );
                if (racketQuantity - unavailableRacket < dto.quantity()) {
                    unavailableRacketNameList.add(productCache.getName());
                    log.info("racketQuantity: " + racketQuantity + " unavailableRacket: " + unavailableRacket + " detail.getQuantity: " + dto.quantity());
                }
            } else {
                Integer productQuantity = productApi.getQuantity(dto.productId());
                Integer orderedProduct = courtOrderRepository.countOrderedProduct(dto.productId(), courtOrder.getOrderDate());
                if (productQuantity - orderedProduct < dto.quantity()) {
                    unavailableProductNameList.add(productCache.getName());
                    log.info("productQuantity: " + productQuantity + " orderedProduct: " + orderedProduct + " detail.getQuantity: " + dto.quantity());
                }
            }
        }
        if (!unavailableRacketNameList.isEmpty()) {
            throw new RuntimeException("Racket " + String.join(", ", unavailableRacketNameList) + " is unavailable");
        } else if (!unavailableProductNameList.isEmpty()) {
            throw new RuntimeException("Product " + String.join(", ", unavailableProductNameList) + " is unavailable");
        }
    }

    private BigDecimal calculateProductTotalBeforeDiscount(CourtOrder courtOrder) {
        BigDecimal productTotalAmount = BigDecimal.ZERO;

        for (ProductOrderDetail detail : courtOrder.getProductOrderDetails()) {
            ProductCache productCache = productCacheRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            productTotalAmount = productTotalAmount.add(productCache.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
        }
        return productTotalAmount;
    }

    private OrderContract.CreatedOrderRes getCreatedOrderRes(CourtOrder order, List<Integer> courtIds, List<OrderContract.ProductOrderDetailReq> productOrderDetailReqs) {
        if (productOrderDetailReqs != null) {
            checkOrderedProduct(order, productOrderDetailReqs);
            List<ProductOrderDetail> productDetails = productOrderDetailReqs.stream().map(this::mapToProductOrderDetail).toList();
            for (ProductOrderDetail detail : productDetails) { order.addProductOrderDetail(detail); }
        }
        List<CourtOrderDetail> courtOrderDetails = courtIds.stream().map(this::mapToCourtOrderDetails).toList();
        for (CourtOrderDetail detail : courtOrderDetails) { order.addCourtOrderDetail(detail); }
        order.setStatus(OrderStatus.WaitingForPayment);
        CourtOrder saved = courtOrderRepository.save(order);
        return new OrderContract.CreatedOrderRes(saved.getCourtOrderId());
    }

    private CourtOrderDetail mapToCourtOrderDetails(Integer courtId) {
        CourtOrderDetail detail = new CourtOrderDetail();
        detail.setCourtId(courtId);
        return detail;
    }

    private ProductOrderDetail mapToProductOrderDetail(OrderContract.ProductOrderDetailReq dto) {
        ProductOrderDetail productOrderDetail = new ProductOrderDetail();
        productOrderDetail.setProductId(dto.productId());
        productOrderDetail.setQuantity(dto.quantity());
        return productOrderDetail;
    }

    private OrderContract.ResByAdmin mapToResponseByAdmin(CourtOrder entity) {
        return new OrderContract.ResByAdmin(
                entity.getCourtOrderId(),
                entity.getOrderDate(),
                entity.getStartHour(),
                entity.getEndHour(),
                entity.getStatus(),
                entity.getAdminId(),
                entity.getMemberId(),
                entity.getGuestName(),
                entity.getGuestEmail(),
                entity.getGuestPhoneNumber(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCourtOrderDetails().stream().map(CourtOrderDetail::getCourtId).toList()
        );
    }

    private OrderContract.ResByCustomer mapToResponseByCustomer(CourtOrder entity) {
        return new OrderContract.ResByCustomer(
                entity.getCourtOrderId(),
                entity.getOrderDate(),
                entity.getStartHour(),
                entity.getEndHour(),
                entity.getStatus(),
                entity.getCourtOrderDetails().stream().map(CourtOrderDetail::getCourtId).toList()
        );
    }
}
