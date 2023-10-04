package com.javajober.spaceWall.dto.request;

import lombok.Getter;

@Getter
public class DeleteTemporaryRequest {

    private Long memberId;
    private Long spaceId;

    private DeleteTemporaryRequest() {
    }

    public DeleteTemporaryRequest(final Long memberId, final Long spaceId) {
        this.memberId = memberId;
        this.spaceId = spaceId;
    }
}
