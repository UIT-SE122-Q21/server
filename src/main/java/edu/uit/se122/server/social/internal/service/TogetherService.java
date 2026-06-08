package edu.uit.se122.server.social.internal.service;

import edu.uit.se122.server.common.enums.TogetherStatus;
import edu.uit.se122.server.social.TogetherContract;
import edu.uit.se122.server.social.internal.entity.ChatDetail;
import edu.uit.se122.server.social.internal.entity.CourtOrderCache;
import edu.uit.se122.server.social.internal.entity.MemberCache;
import edu.uit.se122.server.social.internal.entity.Together;
import edu.uit.se122.server.social.internal.repository.ChatDetailRepository;
import edu.uit.se122.server.social.internal.repository.CourtOrderCacheRepository;
import edu.uit.se122.server.social.internal.repository.MemberCacheRepository;
import edu.uit.se122.server.social.internal.repository.TogetherRepository;
import edu.uit.se122.server.social.internal.state.TogetherState;
import edu.uit.se122.server.social.internal.state.TogetherStateFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TogetherService {
    private final TogetherRepository togetherRepository;
    private final ChatDetailRepository chatDetailRepository;
    private final CourtOrderCacheRepository courtOrderCacheRepository;
    private final MemberCacheRepository memberCacheRepository;
    private final TogetherStateFactory stateFactory;

    public List<TogetherContract.Res> getAll() {
        return togetherRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public TogetherContract.Res getById(Integer id) {
        return togetherRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Together not found"));
    }

    public void create(Integer courtOrderId, TogetherContract.Req dto) {
        Together together = new Together();
        together.setContent(dto.content());
        together.setNumOfPlayersPrefix(dto.numOfPlayersPrefix());
        together.setStatus(TogetherStatus.PENDING);
        together.setCourtOrderId(courtOrderId);
        togetherRepository.save(together);
    }

    public void join(Integer togetherId, Integer memberId) {
        Together together = togetherRepository.findById(togetherId)
                .orElseThrow(() -> new RuntimeException("Together not found"));
        MemberCache member = memberCacheRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        TogetherState currentState = stateFactory.getState(together.getStatus());

        currentState.joinTogether(together, member);
        togetherRepository.save(together);
    }

    public void planManually(Integer togetherId) {
        Together together = togetherRepository.findById(togetherId)
                .orElseThrow(() -> new RuntimeException("Together not found"));
        together.setStatus(TogetherStatus.PLANNED);
    }

    public void cancel(Integer togetherId) {
        Together together = togetherRepository.findById(togetherId)
                .orElseThrow(() -> new RuntimeException("Together not found"));
        TogetherState currentState = stateFactory.getState(together.getStatus());
        currentState.cancelTogether(together);
        togetherRepository.save(together);
    }

    public ChatDetail saveChatMessage(Integer togetherId, Integer memberId, String message) {
        Together together = togetherRepository.findById(togetherId)
                .orElseThrow(() -> new RuntimeException("Together not found"));
        TogetherState currentState = stateFactory.getState(together.getStatus());
        return currentState.saveChatMessage(together, memberId, message);
    }

    public ChatDetail joinChat(Integer togetherId, Integer memberId, String message) {
        Together together = togetherRepository.findById(togetherId)
                .orElseThrow(() -> new RuntimeException("Together not found"));
        TogetherState currentState = stateFactory.getState(together.getStatus());
        return currentState.joinChat(together, memberId, message);
    }

    public ChatDetail leaveChat(Integer togetherId, Integer memberId, String message) {
        Together together = togetherRepository.findById(togetherId)
                .orElseThrow(() -> new RuntimeException("Together not found"));
        TogetherState currentState = stateFactory.getState(together.getStatus());
        return currentState.leaveChat(together, memberId, message);
    }

    public List<ChatDetail> getChatMessages(Integer togetherId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return chatDetailRepository.findByTogetherIdOrderByCreatedAtAsc(togetherId, pageable).getContent();
    }

    private TogetherContract.Res mapToDTO(Together entity) {
        List<Integer> memberIds = entity.getTogetherMembers().stream()
                .map(togetherMember -> togetherMember.getMember().getMemberId())
                .toList();
        CourtOrderCache courtOrderCache = courtOrderCacheRepository.findById(entity.getCourtOrderId())
                .orElseThrow(() -> new RuntimeException("Court order not found"));

        return new TogetherContract.Res(
                entity.getTogetherId(),
                entity.getStatus(),
                entity.getContent(),
                entity.getNumOfPlayersPrefix(),
                entity.getNumOfPlayersJoined(),
                courtOrderCache.getOrderDate(),
                courtOrderCache.getStartHour(),
                courtOrderCache.getEndHour(),
                memberIds
        );
    }
}
