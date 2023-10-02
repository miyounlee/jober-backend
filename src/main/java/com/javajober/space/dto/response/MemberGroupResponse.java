package com.javajober.space.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberGroupResponse {
    private Long memberId;
    private String memberName;
    private String memberHashtag;
    private String accountType;
    private String phoneNumber;

    private MemberGroupResponse() {
    }

    @Builder
    public MemberGroupResponse(final Long memberId, final String memberName, final String memberHashtag, final String accountType, final String phoneNumber) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberHashtag = memberHashtag;
        this.accountType = accountType;
        this.phoneNumber = phoneNumber;
    }
}
