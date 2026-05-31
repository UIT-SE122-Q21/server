package edu.uit.se122.server.social.internal.service;

import edu.uit.se122.server.common.enums.TogetherStatus;
import edu.uit.se122.server.social.TogetherContract;
import edu.uit.se122.server.social.internal.entity.CourtOrderCache;
import edu.uit.se122.server.social.internal.entity.MemberCache;
import edu.uit.se122.server.social.internal.entity.Together;
import edu.uit.se122.server.social.internal.entity.TogetherMember;
import edu.uit.se122.server.social.internal.repository.CourtOrderCacheRepository;
import edu.uit.se122.server.social.internal.repository.MemberCacheRepository;
import edu.uit.se122.server.social.internal.repository.TogetherMemberRepository;
import edu.uit.se122.server.social.internal.repository.TogetherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TogetherService {
    private final TogetherRepository togetherRepository;
    private final CourtOrderCacheRepository courtOrderCacheRepository;
    private final MemberCacheRepository memberCacheRepository;
    private final TogetherMemberRepository togetherMemberRepository;

    public List<TogetherContract.Res> getAll() {
        return togetherRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public TogetherContract.Res getById(Integer id) {
        return togetherRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Together not found"));
    }

    public void create(TogetherContract.Req dto) {
        Together together = new Together();
        together.setContent(dto.content());
        together.setNumOfPlayersPrefix(dto.numOfPlayersPrefix());
        together.setStatus(TogetherStatus.Pending);
        togetherRepository.save(together);
    }

    public void join(Integer togetherId, Integer memberId) {
        Together together = togetherRepository.findById(togetherId)
                .orElseThrow(() -> new RuntimeException("Together not found"));

        if (!together.getStatus().equals(TogetherStatus.Pending)) {
            throw new RuntimeException("Together is not available");
        }

        MemberCache member = memberCacheRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (!togetherMemberRepository.checkMemberInTogether(togetherId, memberId)) {
            TogetherMember togetherMember = new TogetherMember();
            togetherMember.setTogether(together);
            togetherMember.setMember(member);
            together.getTogetherMembers().add(togetherMember);
            together.setNumOfPlayersJoined(together.getNumOfPlayersJoined() + 1);
            if (Objects.equals(together.getNumOfPlayersJoined(), together.getNumOfPlayersPrefix())) {
                together.setStatus(TogetherStatus.Assemble);
            }
        }
    }

    public void plan(Integer togetherId) {
        Together together = togetherRepository.findById(togetherId)
                .orElseThrow(() -> new RuntimeException("Together not found"));
        together.setStatus(TogetherStatus.Planned);
    }

    public void delete(Integer id) { togetherRepository.deleteById(id); }

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
