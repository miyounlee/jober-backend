package com.javajober.freeBlock.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class FreeBlockSaveRequests {

    List<FreeBlockSaveRequest> subData;

    private FreeBlockSaveRequests() {
    }

    public FreeBlockSaveRequests(List<FreeBlockSaveRequest> subData) {
        this.subData = subData;
    }
}
