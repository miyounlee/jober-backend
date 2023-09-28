package com.javajober.fileBlock.dto.response;

import com.javajober.fileBlock.domain.FileBlock;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FileBlockResponse {

    private String fileTitle;
    private String fileDescription;
    private String fileName;

    private FileBlockResponse() {

    }

    @Builder
    public FileBlockResponse(final String fileTitle, final String fileDescription, final String fileName) {
        this.fileTitle = fileTitle;
        this.fileDescription = fileDescription;
        this.fileName = fileName;
    }

    public static FileBlockResponse from(FileBlock fileBlock) {
        return FileBlockResponse.builder()
                .fileTitle(fileBlock.getFileTitle())
                .fileDescription(fileBlock.getFileDescription())
                .fileName(fileBlock.getFileName())
                .build();
    }
}
