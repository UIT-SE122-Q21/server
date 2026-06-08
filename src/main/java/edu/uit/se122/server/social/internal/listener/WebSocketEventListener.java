package edu.uit.se122.server.social.internal.listener;

import edu.uit.se122.server.social.internal.entity.ChatDetail;
import edu.uit.se122.server.social.internal.service.TogetherService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final TogetherService togetherService;
    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        if (headerAccessor.getSessionAttributes() != null) {
            String sessionMemberId = (String) headerAccessor.getSessionAttributes().get("memberId");
            Integer togetherId = (Integer) headerAccessor.getSessionAttributes().get("togetherId");

            if (sessionMemberId != null && togetherId != null) {
                Integer memberId = Integer.parseInt(sessionMemberId);
                String leaveMessage = "User " + memberId + " has left the group chat due to disconnection";

                // Kích hoạt nghiệp vụ đổi trạng thái rời phòng sang MongoDB thông qua State Pattern
                ChatDetail savedLog = togetherService.leaveChat(togetherId, memberId, leaveMessage);

                // ÉP ĐƯỜNG DẪN CHUẨN XÁC KHI PHÁT TÁN TIN BÁO TỬ
                messagingTemplate.convertAndSend("/topic/together." + togetherId, savedLog);
            }
        }
    }
}
