package edu.uit.se122.server.social.internal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TogetherMember")
@Data
public class TogetherMember {
    @EmbeddedId
    private TogetherMemberId id = new TogetherMemberId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("togetherId")
    @JoinColumn(name = "together_id")
    private Together together;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private MemberCache member;
}
