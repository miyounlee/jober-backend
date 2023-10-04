package com.javajober.spaceWall.dto.request;

import com.javajober.styleSetting.dto.request.StyleSettingStringRequest;
import com.javajober.wallInfoBlock.dto.request.WallInfoBlockStringRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class DataStringRequest {

    private String category;
    private Long memberId;
    private Long spaceId;
    private String shareURL;
    private WallInfoBlockStringRequest wallInfoBlock;
    private List<BlockRequest> blocks;
    private StyleSettingStringRequest styleSetting;

    private DataStringRequest() {
    }

    public DataStringRequest(final String category, final Long memberId, Long spaceId, final String shareURL,
                             final WallInfoBlockStringRequest wallInfoBlock, final List<BlockRequest> blocks,
                             final StyleSettingStringRequest styleSetting) {
        this.category = category;
        this.memberId = memberId;
        this.spaceId = spaceId;
        this.shareURL = shareURL;
        this.wallInfoBlock = wallInfoBlock;
        this.blocks = blocks;
        this.styleSetting = styleSetting;
    }
}
