package com.javajober.wallInfoBlock.dto.request;

import com.javajober.wallInfoBlock.domain.WallInfoBlock;
import lombok.Getter;

@Getter
public class WallInfoBlockStringRequest {

    private String wallInfoTitle;
    private String wallInfoDescription;
    private String backgroundImgURL;
    private String wallInfoImgURL;


    private WallInfoBlockStringRequest() {

    }

    public WallInfoBlockStringRequest(final String wallInfoTitle, final String wallInfoDescription, final String backgroundImgURL,
                                      final String wallInfoImgURL) {
        this.wallInfoTitle = wallInfoTitle;
        this.wallInfoDescription = wallInfoDescription;
        this.backgroundImgURL = backgroundImgURL;
        this.wallInfoImgURL = wallInfoImgURL;
    }

    public static WallInfoBlock toEntity(final WallInfoBlockStringRequest wallInfoBlockStringRequest) {
        return WallInfoBlock.builder()
                .wallInfoTitle(wallInfoBlockStringRequest.getWallInfoTitle())
                .wallInfoDescription(wallInfoBlockStringRequest.getWallInfoDescription())
                .wallInfoBackgroundImageUrl(wallInfoBlockStringRequest.getBackgroundImgURL())
                .wallInfoProfileImageUrl(wallInfoBlockStringRequest.getWallInfoImgURL())
                .build();
    }
}
