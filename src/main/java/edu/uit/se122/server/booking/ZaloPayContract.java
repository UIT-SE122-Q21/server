package edu.uit.se122.server.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

public interface ZaloPayContract {
    @Data
    @Builder
    class OrderReq {
        @JsonProperty("app_id") private Integer appId;
        @JsonProperty("app_trans_id") private String appTransId;
        @JsonProperty("app_user") private String appUser;
        @JsonProperty("app_time") private Long appTime;
        @JsonProperty("amount") private Long amount;
        @JsonProperty("embed_data") private String embedData;
        @JsonProperty("item") private String item;
        @JsonProperty("description") private String description;
        @JsonProperty("mac") private String mac;
        @JsonProperty("callback_url") private String callbackUrl;
    }

    record OrderRes(
            @JsonProperty("return_code") Integer returnCode,
            @JsonProperty("return_message") String returnMessage,
            @JsonProperty("sub_return_code") Integer subReturnCode,
            @JsonProperty("sub_return_message") String subReturnMessage,
            @JsonProperty("zp_trans_token") String zpTransToken,
            @JsonProperty("order_url") String orderUrl,
            @JsonProperty("order_token") String orderToken
    ) {}

    record Callback(
            String data,
            String mac,
            Integer type
    ) {}

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true) // Bỏ qua các trường không cần thiết khác nếu có
    class CallbackData {
        @JsonProperty("app_id") private Integer appId;
        @JsonProperty("app_trans_id") private String appTransId;
        @JsonProperty("app_time") private Long appTime;
        @JsonProperty("app_user") private String appUser;
        @JsonProperty("amount") private BigDecimal amount;
        @JsonProperty("embed_data") private String embedData;
        @JsonProperty("item") private String item;
        @JsonProperty("zp_trans_id") private Long zpTransId;
        @JsonProperty("server_time") private Long serverTime;
        @JsonProperty("channel") private Integer channel;
        @JsonProperty("merchant_user_id") private String merchantUserId;
        @JsonProperty("user_fee_amount") private Long userFeeAmount;
        @JsonProperty("discount_amount") private Long discountAmount;
    }

    record CallbackStatusRes(
            @JsonProperty("return_code") int returnCode,
            @JsonProperty("return_message") String returnMessage
    ) {}

    @Data
    @Builder
    class RefundReq {
        @JsonProperty("m_refund_id") String mRefundId;
        @JsonProperty("app_id") Integer appId;
        @JsonProperty("zp_trans_id") String zpTransId;
        @JsonProperty("amount") Long amount;
        @JsonProperty("timestamp") Long timestamp;
        @JsonProperty("mac") String mac;
        @JsonProperty("description") String description;
    }

    record RefundRes(
            @JsonProperty("return_code") Integer returnCode,
            @JsonProperty("return_message") String returnMessage,
            @JsonProperty("sub_return_code") Integer subReturnCode,
            @JsonProperty("sub_return_message") String subReturnMessage,
            @JsonProperty("refund_id") Long refundId
    ) {}

    @Data
    @Builder
    class QueryRefundReq {
        @JsonProperty("app_id") Integer appId;
        @JsonProperty("m_refund_id") String mRefundId;
        @JsonProperty("timestamp") Long timestamp;
        @JsonProperty("mac") String mac;
    }

    record QueryRefundRes(
            Integer returnCode,
            String returnMessage,
            Integer subReturnCode,
            String subReturnMessage
    ) {}
}
