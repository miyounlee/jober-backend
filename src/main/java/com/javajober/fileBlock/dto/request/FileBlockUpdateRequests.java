package com.javajober.fileBlock.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class FileBlockUpdateRequests {

    List<FileBlockUpdateRequest> subData;

    private FileBlockUpdateRequests() {}

}
