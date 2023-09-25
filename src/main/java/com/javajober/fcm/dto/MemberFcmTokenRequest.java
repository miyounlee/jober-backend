package com.javajober.fcm.dto;

import com.javajober.member.domain.Member;
import com.javajober.fcm.domain.MemberFcmToken;
import lombok.Getter;

@Getter
public class MemberFcmTokenRequest {

    private Long memberId;
    private String fcmToken;
    private String deviceId;

    public MemberFcmTokenRequest() {
    }

    public static MemberFcmToken toEntity(MemberFcmTokenRequest request, Member member) {
        return MemberFcmToken.builder()
                .member(member)
                .fcmToken(request.getFcmToken())
                .deviceId(request.getDeviceId())
                .build();
    }
}