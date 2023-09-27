package com.javajober.freeBlock.dto.response;

import com.javajober.freeBlock.domain.FreeBlock;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FreeBlockResponse {

    private Long freeId;
    private String freeTitle;
    private String freeContent;

    private FreeBlockResponse() {

    }

    @Builder
    public FreeBlockResponse(final Long freeId, final String freeTitle, final String freeContent) {
        this.freeId = freeId;
        this.freeTitle = freeTitle;
        this.freeContent = freeContent;
    }

    public static FreeBlockResponse from(FreeBlock freeBlock) {
        return FreeBlockResponse.builder()
                .freeId(freeBlock.getId())
                .freeTitle(freeBlock.getFreeTitle())
                .freeContent(freeBlock.getFreeContent())
                .build();
    }
}
