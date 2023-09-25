package com.javajober.spaceWall.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SpaceWallResponse {

    private Long spaceWallId;
    private boolean hasWallTemporary;

    public static SpaceWallResponse from(Long spaceWallId, boolean hasWallTemporary) {

        return SpaceWallResponse.builder()
                .spaceWallId(spaceWallId)
                .hasWallTemporary(hasWallTemporary)
                .build();
    }
}
