package com.javajober.fileBlock.dto.request;

import com.javajober.fileBlock.domain.FileBlock;
import lombok.Getter;

@Getter
public class FileBlockSaveRequest {

    private String fileTitle;
    private String fileDescription;

    public FileBlockSaveRequest() {
    }

    public static FileBlock toEntity(FileBlockSaveRequest fileBlockSaveRequest) {
        return FileBlock.builder()
                .fileTitle(fileBlockSaveRequest.getFileTitle())
                .fileDescription(fileBlockSaveRequest.getFileDescription())
                .build();
    }
}
