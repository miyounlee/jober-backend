package com.javajober.spaceWall.controller;

import com.javajober.core.message.SuccessMessage;
import com.javajober.core.util.ApiUtils;
import com.javajober.spaceWall.dto.response.SpaceWallResponse;
import com.javajober.spaceWall.service.SpaceWallService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpaceWallController {

    private final SpaceWallService spaceWallService;

    public SpaceWallController(SpaceWallService spaceWallService) {
        this.spaceWallService = spaceWallService;
    }

    @GetMapping("/api/wall-temporary/storage/{memberId}/{addSpaceId}")
    public ResponseEntity<ApiUtils.ApiResponse<SpaceWallResponse>> checkSpaceWallTemporary(@PathVariable Long memberId, @PathVariable Long addSpaceId) {

        SpaceWallResponse response = spaceWallService.checkSpaceWallTemporary(memberId, addSpaceId);

        return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.SPACE_WALL_TEMPORARY_QUERY_SUCCESS, response));
    }
}
