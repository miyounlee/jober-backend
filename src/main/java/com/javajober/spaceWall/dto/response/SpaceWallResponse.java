package com.javajober.spaceWall.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SpaceWallResponse {

    private Long spaceWallId;
    private boolean hasWallTemporary;

    private SpaceWallResponse() {

    }

    @Builder
    public SpaceWallResponse(final Long spaceWallId, final boolean hasWallTemporary) {
        this.spaceWallId = spaceWallId;
        this.hasWallTemporary = hasWallTemporary;
    }
}

