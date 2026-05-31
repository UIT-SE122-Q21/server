package edu.uit.se122.server.social.internal.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class TogetherMemberId implements Serializable {
    private Integer togetherId;
    private Integer memberId;
}
