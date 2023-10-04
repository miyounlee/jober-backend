package com.javajober.space.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DataResponse {
    private boolean hasWall;
    private List<MemberGroupResponse> list;

    @Builder
    public DataResponse(final boolean hasWall, final List<MemberGroupResponse> list) {
        this.hasWall = hasWall;
        this.list = list;
    }
}