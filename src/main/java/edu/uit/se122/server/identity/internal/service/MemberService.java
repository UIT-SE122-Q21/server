package edu.uit.se122.server.identity.internal.service;

import edu.uit.se122.server.identity.MemberContract;
import edu.uit.se122.server.identity.internal.entity.Member;
import edu.uit.se122.server.identity.internal.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public List<MemberContract.Res> getAll() {
        return memberRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MemberContract.Res getById(Integer id) {
        return memberRepository.findById(id).map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }

    private MemberContract.Res mapToResponse(Member entity) {
        return new MemberContract.Res(
                entity.getMemberId(),
                entity.getName(),
                entity.getEmail(),
                entity.getVerified(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
