package edu.uit.se122.server.social;

import edu.uit.se122.server.social.internal.entity.ChatDetail;
import edu.uit.se122.server.social.internal.repository.ChatDetailRepository;
import edu.uit.se122.server.social.internal.service.TogetherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/member/together")
@RequiredArgsConstructor
public class TogetherController {
    private final TogetherService togetherService;
    private final SimpMessageSendingOperations messagingTemplate;

    @GetMapping
    public ResponseEntity<List<TogetherContract.Res>> getAll() {
        return ResponseEntity.ok(togetherService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TogetherContract.Res> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(togetherService.getById(id));
    }

    @PostMapping("/{courtOrderId}")
    public ResponseEntity<Object> create(@PathVariable Integer courtOrderId, @RequestBody TogetherContract.Req dto) {
        togetherService.create(courtOrderId, dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> join(@PathVariable Integer id, @AuthenticationPrincipal Integer memberId) {
        togetherService.join(id, memberId);
        return ResponseEntity.ok(Map.of("message", "Tham gia cuộc hẹn thành công"));
    }

    @PutMapping("/{togetherId}/plan-manually")
    public ResponseEntity<Object> planManually(@PathVariable Integer togetherId) {
        togetherService.planManually(togetherId);
        return ResponseEntity.ok(Map.of("message", "Đã chốt hẹn"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> cancel(@PathVariable Integer id) {
        togetherService.cancel(id);
        return ResponseEntity.ok(Map.of("message", "Hủy cuộc hẹn thành công"));
    }

    @MessageMapping("/together.chat/{togetherId}")
    public void sendGroupMessage(
            @DestinationVariable Integer togetherId,
            @Payload String incomingMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        String sessionMemberId = (String) headerAccessor.getSessionAttributes().get("memberId");
        if (sessionMemberId == null) return;

        // 2. Chuyển đổi an toàn sang Integer cho tầng Service xử lý nghiệp vụ & State
        Integer memberId = Integer.parseInt(sessionMemberId);

        // 3. Thực thi nghiệp vụ lưu dữ liệu MongoDB qua State tương ứng
        ChatDetail savedChat = togetherService.saveChatMessage(togetherId, memberId, incomingMessage);

        // 4. CHỦ ĐỘNG đẩy tin nhắn qua đường dẫn chuẩn tuyệt đối sang RabbitMQ
        String destination = "/topic/together." + togetherId;
        messagingTemplate.convertAndSend(destination, savedChat);
    }

    @MessageMapping("/together.addUser/{togetherId}")
    public void addUser(
            @DestinationVariable Integer togetherId,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        String sessionMemberId = (String) headerAccessor.getSessionAttributes().get("memberId");
        if (sessionMemberId == null) return;

        Integer memberId = Integer.parseInt(sessionMemberId);

        // Giữ vết togetherId cho Listener dọn dẹp khi rớt mạng đột ngột
        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("togetherId", togetherId);
        }

        // Tạo tin nhắn hệ thống thông báo gia nhập nhóm
        String sysNotification = "User " + memberId + " has joined the group chat";
        ChatDetail savedChat = togetherService.joinChat(togetherId, memberId, sysNotification);

        // CHỦ ĐỘNG đẩy thông báo hệ thống sang RabbitMQ
        String destination = "/topic/together." + togetherId;
        messagingTemplate.convertAndSend(destination, savedChat);
    }

    // 2. HTTP REST API: Lấy lịch sử chat cũ khi người chơi vừa mở màn hình cuộc hẹn lên
    @GetMapping("/{togetherId}/chat-history")
    @ResponseBody
    public List<ChatDetail> getHistory(
            @PathVariable Integer togetherId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return togetherService.getChatMessages(togetherId, page, size);
    }
}
