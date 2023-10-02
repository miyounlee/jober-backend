package com.javajober.spaceWall.dto.response;

import com.javajober.core.util.CommonResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class BlockResponse<T extends CommonResponse> {
    /**
     * T 에는 entity별 dto가 옴
     * ex) List<FreeBlockResponse> subData
     * 단일 건인 FreeBlock과 FileBlock은 List의 크기가 1일 것!
     * 조회 후 dto로 변환한 애들을 subData에 추가
     */
    private String blockUUID;
    private String blockType;
    private List<T> subData;

    private BlockResponse() {}

    @Builder
    public BlockResponse(final String blockUUID, final String blockType, final List<T> subData) {
        this.blockUUID = blockUUID;
        this.blockType = blockType;
        this.subData = subData;
    }

}
