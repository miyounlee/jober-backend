package com.javajober.fileBlock.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class FileBlockStringRequests {

    List<FileBlockStringRequest> subData;

    private FileBlockStringRequests() {}

    public FileBlockStringRequests(List<FileBlockStringRequest> subData) {
        this.subData = subData;
    }
}
