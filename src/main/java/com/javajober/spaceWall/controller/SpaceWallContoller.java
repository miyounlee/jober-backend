package com.javajober.spaceWall.controller;

import com.javajober.core.util.ApiUtils;
import com.javajober.spaceWall.dto.response.SpaceWallResponse;
import com.javajober.spaceWall.service.SpaceWallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SpaceWallContoller {

    private final SpaceWallService spaceWallService;

    @GetMapping("/api/wall-temporary/storage/{memberId}/{addSpaceId}")
    public ResponseEntity<?> checkSpaceWallTemporary(@PathVariable Long memberId, @PathVariable Long addSpaceId) {

        SpaceWallResponse response = spaceWallService.checkSpaceWallTemporary(memberId, addSpaceId);

        return ResponseEntity.ok(ApiUtils.success(response));
    }
}
