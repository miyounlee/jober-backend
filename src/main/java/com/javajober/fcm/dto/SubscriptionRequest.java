package com.javajober.fcm.dto;

import com.javajober.fcm.domain.Subscription;
import com.javajober.member.domain.Member;
import com.javajober.spaceWall.domain.SpaceWall;
import lombok.Getter;

@Getter
public class SubscriptionRequest {

    private Long memberId;
    private Long subscriberMemberId;
    private Long spaceWallId;

    public SubscriptionRequest() {
    }

    public static Subscription toEntity(SubscriptionRequest request, Member subscriber, SpaceWall spaceWall) {
        return new Subscription(subscriber, spaceWall);
    }
}