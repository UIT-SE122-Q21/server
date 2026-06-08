package edu.uit.se122.server.social.internal.config;

import edu.uit.se122.server.common.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${chat.relay.host}")
    private String relayHost;

    @Value("${chat.relay.port}")
    private int relayPort;

    @Value("${spring.rabbitmq.username}")
    private String clientLogin;

    @Value("${spring.rabbitmq.password}")
    private String clientPassword;

    private final JwtService jwtService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new CustomHandshakeInterceptor(jwtService))
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Thiết lập prefix cho các API xử lý logic tại Controller
        registry.setApplicationDestinationPrefixes("/app");

        // THAY THẾ: Bật bộ chuyển tiếp Stomp Broker Relay tới RabbitMQ
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(relayHost)
                .setRelayPort(relayPort)
                .setClientLogin(clientLogin)
                .setClientPasscode(clientPassword)
                .setSystemLogin(clientLogin)
                .setSystemPasscode(clientPassword);
    }
}
