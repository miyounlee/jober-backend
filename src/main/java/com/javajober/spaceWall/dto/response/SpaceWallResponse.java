package com.javajober.spaceWall.dto.response;

import com.javajober.core.util.response.CommonResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SpaceWallResponse {

    private String category;
    private Long spaceId;
    private Long spaceWallId;
    private Long memberId;
    private Boolean isPublic;
    private String shareURL;
    private CommonResponse wallInfoBlock;
    private List<BlockResponse<CommonResponse>> blocks;
    private CommonResponse styleSetting;

    private SpaceWallResponse() {}

    @Builder
    public SpaceWallResponse(final String category, final Long spaceId, final Long spaceWallId, final Long memberId, final Boolean isPublic, final String shareURL, final CommonResponse wallInfoBlock,
                             final List<BlockResponse<CommonResponse>> blocks, final CommonResponse styleSetting) {

        this.category = category;
        this.spaceId = spaceId;
        this.spaceWallId = spaceWallId;
        this.memberId = memberId;
        this.isPublic = isPublic;
        this.shareURL = shareURL;
        this.wallInfoBlock = wallInfoBlock;
        this.blocks = blocks;
        this.styleSetting = styleSetting;
    }

}