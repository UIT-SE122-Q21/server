package edu.uit.se122.server.booking.internal.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.zalo-pay")
@Data
public class ZaloPayProperties {
    private String appId;
    private String key1;
    private String key2;
    private String createUrl;
    private String refundUrl;
    private String queryRefundUrl;
    private String callbackUrl;
    private String returnUrl;
}
