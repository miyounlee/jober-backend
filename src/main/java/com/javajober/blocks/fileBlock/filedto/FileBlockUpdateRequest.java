package com.javajober.blocks.fileBlock.filedto;

import com.javajober.blocks.fileBlock.domain.FileBlock;
import com.javajober.blocks.fileBlock.dto.request.FileBlockStringUpdateRequest;

import lombok.Getter;

@Getter
public class FileBlockUpdateRequest {

    private Long fileBlockId;
    private String fileTitle;
    private String fileDescription;

    public FileBlockUpdateRequest() {

    }

    public static FileBlock toEntity(final FileBlockUpdateRequest request, String file) {
        return FileBlock.builder()
            .fileTitle(request.getFileTitle())
            .fileDescription(request.getFileDescription())
            .file(file)
            .build();
    }
}