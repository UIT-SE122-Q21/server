package edu.uit.se122.server.social.internal.state;

import edu.uit.se122.server.common.enums.MessageType;
import edu.uit.se122.server.common.enums.TogetherStatus;
import edu.uit.se122.server.social.internal.entity.ChatDetail;
import edu.uit.se122.server.social.internal.entity.MemberCache;
import edu.uit.se122.server.social.internal.entity.Together;
import edu.uit.se122.server.social.internal.repository.ChatDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlannedState implements TogetherState {
    private final ChatDetailRepository chatDetailRepository;

    @Override
    public void joinTogether(Together together, MemberCache member) {
        throw new IllegalStateException("Together is already planned! Please wait for another");
    }

    @Override
    public void checkOutOrder(Together together) {
        together.setStatus(TogetherStatus.COMPLETED);
        log.info("Together {} is completed", together.getTogetherId());
    }

    @Override
    public void cancelTogether(Together together) {
        throw new IllegalStateException("Together is already planned! It cannot be canceled");
    }

    @Override
    public ChatDetail saveChatMessage(Together together, Integer memberId, String message) {
        ChatDetail chatDetail = new ChatDetail();
        chatDetail.setTogetherId(together.getTogetherId());
        chatDetail.setMemberId(memberId);
        chatDetail.setContent(message);
        chatDetail.setType(MessageType.CHAT);
        chatDetailRepository.save(chatDetail);
        return chatDetail;
    }

    @Override
    public ChatDetail joinChat(Together together, Integer memberId, String message) {
        ChatDetail chatDetail = new ChatDetail();
        chatDetail.setTogetherId(together.getTogetherId());
        chatDetail.setMemberId(memberId);
        chatDetail.setContent(message);
        chatDetail.setType(MessageType.JOIN);
        chatDetailRepository.save(chatDetail);
        return chatDetail;
    }

    @Override
    public ChatDetail leaveChat(Together together, Integer memberId, String message) {
        ChatDetail chatDetail = new ChatDetail();
        chatDetail.setTogetherId(together.getTogetherId());
        chatDetail.setMemberId(memberId);
        chatDetail.setContent(message);
        chatDetail.setType(MessageType.LEAVE);
        chatDetailRepository.save(chatDetail);
        return chatDetail;
    }
}
