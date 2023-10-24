package com.javajober.space.dto.request;

import com.javajober.member.domain.Member;
import com.javajober.space.domain.AddSpace;
import com.javajober.space.domain.SpaceType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SpaceSaveRequest {

    private Long memberId;
    private String spaceTitle;
    private String representativeName;
    private String spaceType;

    private SpaceSaveRequest() {
    }

    @Builder
    public SpaceSaveRequest(Long memberId, String spaceTitle, String representativeName, String spaceType) {
        this.memberId = memberId;
        this.spaceTitle = spaceTitle;
        this.representativeName = representativeName;
        this.spaceType = spaceType;
    }

    public static AddSpace toEntity(SpaceSaveRequest request, Member member) {
        return AddSpace.builder()
                .spaceTitle(request.getSpaceTitle())
                .spaceType(SpaceType.findSpaceTypeByString(request.getSpaceType()))
                .representativeName(request.getRepresentativeName())
                .member(member)
                .build();
    }
}
