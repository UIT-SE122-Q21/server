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

    public List<OrderContract.Res> getAll() {
        return courtOrderRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public OrderContract.Res getById(Integer id) {
        return courtOrderRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<OrderContract.OrderHistoryRes> getOrderHistory() {
        return courtOrderRepository.findAll().stream().map(this::mapToOrderHistoryDTO).toList();
    }

    public OrderContract.CreatedOrderRes createOrderByAdmin(Integer adminId, OrderContract.CreateOrderByAdminReq dto) {
        CourtOrder order = new CourtOrder();
        order.setOrderDate(dto.orderDate());
        order.setStartHour(dto.startHour());
        order.setEndHour(dto.endHour());
        order.setAdminId(adminId);
        return getCreatedOrderRes(order, dto.courtIds(), dto.productDetails());
    }

    public OrderContract.CreatedOrderRes createOrderByCustomer(Integer memberId, OrderContract.CreateOrderByCustomerReq dto) {
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
        return getCreatedOrderRes(order, dto.courtIds(), dto.productDetails());
    }

    public void updateProductOrderDetails(Integer courtOrderId, List<OrderContract.ProductOrderDetailReq> dtoList) {
        CourtOrder courtOrder = courtOrderRepository.findById(courtOrderId)
                .orElseThrow(() -> new RuntimeException("Court order not found"));
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
                detail = mapToProductOrderDetail(courtOrder, dto);
                courtOrder.getProductOrderDetails().add(detail);
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
        invoice.setCourtOrder(courtOrder);
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
        invoice.setCourtOrder(courtOrder);
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

    private OrderContract.CreatedOrderRes getCreatedOrderRes(CourtOrder order, List<Integer> integers, List<OrderContract.ProductOrderDetailReq> productOrderDetailReqs) {
        checkOrderedProduct(order, productOrderDetailReqs);
        List<CourtOrderDetail> courtOrderDetails = mapToCourtOrderDetails(order, integers);
        List<ProductOrderDetail> productDetails = productOrderDetailReqs.stream().map(detail -> mapToProductOrderDetail(order, detail)).toList();
        order.setCourtOrderDetails(courtOrderDetails);
        order.setProductOrderDetails(productDetails);
        order.setStatus(OrderStatus.WaitingForPayment);
        CourtOrder saved = courtOrderRepository.save(order);
        return new OrderContract.CreatedOrderRes(saved.getCourtOrderId());
    }

    private List<CourtOrderDetail> mapToCourtOrderDetails(CourtOrder entity, List<Integer> courtIds) {
        return courtIds.stream().map(courtId -> {
            CourtOrderDetail detail = new CourtOrderDetail();
            detail.setCourtId(courtId);
            detail.setCourtOrder(entity);
            return detail;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private ProductOrderDetail mapToProductOrderDetail(CourtOrder entity, OrderContract.ProductOrderDetailReq dto) {
        ProductOrderDetail productOrderDetail = new ProductOrderDetail();
        productOrderDetail.setProductId(dto.productId());
        productOrderDetail.setQuantity(dto.quantity());
        productOrderDetail.setCourtOrder(entity);
        return productOrderDetail;
    }

    private OrderContract.Res mapToDTO(CourtOrder entity) {
        return new OrderContract.Res(
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

    private OrderContract.OrderHistoryRes mapToOrderHistoryDTO(CourtOrder entity) {
        String customerName;
        if (entity.getGuest()) {
            customerName = entity.getGuestName();
        } else {
            MemberCache memberCache = memberCacheRepository.findById(entity.getMemberId())
                    .orElseThrow(() -> new RuntimeException("Member not found"));
            customerName = memberCache.getName();
        }

        return new OrderContract.OrderHistoryRes(
                entity.getOrderDate(),
                customerName,
                entity.getProductOrderDetails().stream().map(d -> new OrderContract.OrderHistoryDetailRes(
                        productCacheRepository.findById(d.getProductId()).orElseThrow(() -> new RuntimeException("Product not found")).getProductId(),
                        productCacheRepository.findById(d.getProductId()).orElseThrow(() -> new RuntimeException("Product not found")).getName(),
                        d.getQuantity()
                )).toList()
        );
    }
}
