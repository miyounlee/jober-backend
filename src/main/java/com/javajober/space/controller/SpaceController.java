package com.javajober.space.controller;

import com.javajober.core.message.SuccessMessage;
import com.javajober.core.util.ApiUtils;
import com.javajober.space.service.SpaceService;
import com.javajober.space.dto.response.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
public class SpaceController {

    private final SpaceService spaceService;

    public SpaceController(final SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping("/{memberId}/{addSpaceId}")
    public ResponseEntity<ApiUtils.ApiResponse<DataResponse>> find (@PathVariable final Long memberId, @PathVariable final Long addSpaceId, @RequestParam final String spaceType) {

        DataResponse data = spaceService.find(memberId, addSpaceId, spaceType);

        return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.SPACE_READ_SUCCESS, data));
    }
}