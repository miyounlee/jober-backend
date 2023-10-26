package com.javajober.spaceWall.dto.request;

import lombok.Getter;

@Getter
public class IsPublicUpdateRequest {

    private Long spaceId;
    private Long spaceWallId;
    private Boolean isPublic;

    public IsPublicUpdateRequest() {
    }

    public IsPublicUpdateRequest(final Long spaceId, final Long spaceWallId, final Boolean isPublic) {
        this.spaceId = spaceId;
        this.spaceWallId = spaceWallId;
        this.isPublic = isPublic;
    }
}
