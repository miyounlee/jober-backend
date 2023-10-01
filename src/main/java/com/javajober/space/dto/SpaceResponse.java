package com.javajober.space.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class SpaceResponse {
    private final boolean hasWall;
    private final List<MemberGroupResponse> list;

    @Builder
    public SpaceResponse(boolean hasWall, List<MemberGroupResponse> list) {
        this.hasWall = hasWall;
        this.list = list;
    }
}