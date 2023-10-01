package com.javajober.space.controller;

import com.javajober.core.message.SuccessMessage;
import com.javajober.core.util.ApiUtils;
import com.javajober.entity.SpaceType;
import com.javajober.space.dto.SpaceResponse;
import com.javajober.space.service.SpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
public class SpaceController {
    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping("/{memberId}/{addSpaceId}")
    public ResponseEntity<ApiUtils.ApiResponse<SpaceResponse>> getSpaceDetails(@PathVariable Long memberId, @PathVariable Long addSpaceId, @RequestParam SpaceType spaceType) {
        SpaceResponse spaceResponse = spaceService.getSpaceDetails(memberId, addSpaceId);

        SuccessMessage successMessage;
        switch (spaceType) {
            case PERSONAL:
                successMessage = SuccessMessage.PERSONAL_SPACE_QUERY_SUCCESS;
                break;
            case ORGANIZATION:
                successMessage = SuccessMessage.ORGANIZATION_SPACE_QUERY_SUCCESS;
                break;
            default:
                throw new IllegalArgumentException("Invalid space type");
        }

        return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, successMessage, spaceResponse));
    }
}
