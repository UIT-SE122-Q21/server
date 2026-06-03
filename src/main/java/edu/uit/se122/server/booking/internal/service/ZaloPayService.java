package edu.uit.se122.server.booking.internal.service;

import edu.uit.se122.server.booking.OrderContract;
import edu.uit.se122.server.booking.ZaloPayContract;
import edu.uit.se122.server.booking.internal.entity.CourtOrder;
import edu.uit.se122.server.booking.internal.entity.CourtOrderDetail;
import edu.uit.se122.server.booking.internal.entity.ProductOrderDetail;
import edu.uit.se122.server.booking.internal.properties.ZaloPayProperties;
import edu.uit.se122.server.booking.internal.repository.CourtOrderRepository;
import edu.uit.se122.server.booking.internal.utils.DateTimeUtils;
import edu.uit.se122.server.booking.internal.utils.HmacMacUtils;
import edu.uit.se122.server.common.enums.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZaloPayService {
    private final RestClient restClient = RestClient.builder()
        .requestFactory(new SimpleClientHttpRequestFactory() {{
            setConnectTimeout((int) Duration.ofSeconds(20).toMillis());
            setReadTimeout((int) Duration.ofSeconds(20).toMillis());
        }})
        .build();
    private final CourtOrderRepository courtOrderRepository;
    private final ZaloPayProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ApplicationEventPublisher eventPublisher;

    public ZaloPayContract.OrderRes createPayment(Integer courtOrderId, OrderContract.CreateInvoiceByAdminReq dto) {
        CourtOrder courtOrder = courtOrderRepository.findById(courtOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Court order not found"));

        LocalDateTime now = LocalDateTime.now();
        String yyMMdd = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
        long appTime = System.currentTimeMillis();
        String appTransId = String.format("%s_%d", yyMMdd, appTime);
        String appUser = courtOrder.getAdminId() != null ? courtOrder.getAdminId().toString() : courtOrder.getMemberId() != null ? courtOrder.getMemberId().toString() : courtOrder.getGuestPhoneNumber() != null ? courtOrder.getGuestPhoneNumber() : "unknown";
        Map<String, String> embedDataMap = new HashMap<>();
        embedDataMap.put("redirecturl", properties.getReturnUrl());
        String embedData = objectMapper.writeValueAsString(embedDataMap);

        List<CourtOrderDetail> courtOrderDetails = courtOrder.getCourtOrderDetails() != null ? courtOrder.getCourtOrderDetails() : List.of();
        List<ProductOrderDetail> productOrderDetails = courtOrder.getProductOrderDetails() != null ? courtOrder.getProductOrderDetails() : List.of();
        List<Object> items = Stream.concat(courtOrderDetails.stream(), productOrderDetails.stream()).toList();
        String item = objectMapper.writeValueAsString(items);
        String raw = String.format("%s|%s|%s|%d|%d|%s|%s",
                properties.getAppId(), appTransId, appUser, dto.givenAmount().longValue(), appTime, embedData, item);
        String mac = HmacMacUtils.hmacSha256Hex(properties.getKey1(), raw);

        ZaloPayContract.OrderReq payload = ZaloPayContract.OrderReq.builder()
                .appId(Integer.valueOf(properties.getAppId()))
                .appTransId(appTransId)
                .appUser(appUser)
                .appTime(appTime)
                .amount(dto.givenAmount().longValue())
                .embedData(embedData)
                .item(item)
                .description("Thanh toán ZaloPay cho đơn hàng" + DateTimeUtils.formatToString(courtOrder.getCreatedAt()))
                .mac(mac)
                .callbackUrl(properties.getCallbackUrl())
                .build();
        log.info("ZaloPayOrderRequest: {}", payload);

        ZaloPayContract.OrderRes zaloPayRes = restClient.post()
                .uri(properties.getCreateUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(ZaloPayContract.OrderRes.class);
        if (zaloPayRes != null) {
            log.info("ZaloPayOrderResponse: {}", zaloPayRes);
            courtOrder.setAppTransId(appTransId);
            courtOrderRepository.save(courtOrder);
        }

        return zaloPayRes;
    }

    @Transactional
    public ZaloPayContract.CallbackStatusRes handlePaymentCallback(ZaloPayContract.Callback callback) {
        log.info("📩 Callback received: {}", objectMapper.writeValueAsString(callback));
        String data = callback.data();
        String mac = callback.mac();

        // 1. Kiểm tra dữ liệu đầu vào bắt buộc
        if (data == null || mac == null) {
            throw new IllegalArgumentException("Missing required fields: data or mac");
        }

        // 2. Tính toán MAC kiểm tra tính toàn vẹn (Sử dụng KEY2)
        String calculatedMac = HmacMacUtils.hmacSha256Hex(properties.getKey2(), data);
        if (!calculatedMac.equalsIgnoreCase(mac)) {
            log.warn("❌ MAC validation failed! Calculated: {}, Received: {}", calculatedMac, mac);
            throw new IllegalArgumentException("MAC validation failed");
        }

        // 3. Parse chuỗi JSON dữ liệu 'data' thành Object cụ thể
        ZaloPayContract.CallbackData parsedData = objectMapper.readValue(data, ZaloPayContract.CallbackData.class);
        String appTransId = parsedData.getAppTransId();

        CourtOrder courtOrder = courtOrderRepository.findByAppTransId(appTransId)
                .orElseThrow(() -> new IllegalArgumentException("Court order not found for transaction ID: " + appTransId));
        courtOrder.setZpTransId(parsedData.getZpTransId());
        log.info("Court order found: {}", courtOrder);
        if (courtOrder.getStatus() == OrderStatus.WAITING_FOR_PAYMENT) {
            courtOrder.setStatus(OrderStatus.ORDERED);
            OrderContract.CreatedOrderEvent event = new OrderContract.CreatedOrderEvent(
                    courtOrder.getCourtOrderId(),
                    courtOrder.getOrderDate(),
                    courtOrder.getStartHour(),
                    courtOrder.getEndHour()
            );
            eventPublisher.publishEvent(event);
        }

        return new ZaloPayContract.CallbackStatusRes(1, "OK");
    }

    public void handleRefund(Integer courtOrderId, BigDecimal depositAmount) {
        CourtOrder courtOrder = courtOrderRepository.findById(courtOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Court order not found"));

        LocalDateTime now = LocalDateTime.now();
        String yyMMdd = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String mRefundId = yyMMdd + "_" + properties.getAppId() + "_" + (System.currentTimeMillis() % 100000);
        Long amount = (depositAmount.multiply(BigDecimal.valueOf(0.5))).longValue();
        long timestamp = System.currentTimeMillis();
        String description = "Hoàn tiền mã đơn " + courtOrderId;
        String raw;
        // Trường hợp CÓ phí hoàn
        raw = String.format("%d|%d|%d|%s|%d",
                Integer.valueOf(properties.getAppId()), courtOrder.getZpTransId(), amount, description, timestamp);
        String mac = HmacMacUtils.hmacSha256Hex(properties.getKey1(), raw);

        ZaloPayContract.RefundReq payload = ZaloPayContract.RefundReq.builder()
                .mRefundId(mRefundId)
                .appId(Integer.valueOf(properties.getAppId()))
                .zpTransId(courtOrder.getZpTransId().toString())
                .amount(amount)
                .timestamp(timestamp)
                .mac(mac)
                .description(description)
                .build();
        ZaloPayContract.RefundRes refundRes = restClient.post()
                .uri(properties.getRefundUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(ZaloPayContract.RefundRes.class);
        if (refundRes != null && (refundRes.returnCode() == 1 || refundRes.returnCode() == 3)) {
            courtOrder.setRefundId(refundRes.refundId());
            courtOrder.setStatus(OrderStatus.CANCELED);
        }
        courtOrderRepository.save(courtOrder);
    }
}
