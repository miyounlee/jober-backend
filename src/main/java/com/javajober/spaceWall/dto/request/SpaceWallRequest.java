package com.javajober.spaceWall.dto.request;

import lombok.Getter;

@Getter
public class SpaceWallRequest {
    private DataRequest data;

    private SpaceWallRequest() {

    }

    public SpaceWallRequest(final DataRequest data) {
        this.data = data;
    }
}
