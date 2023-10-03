package com.javajober.spaceWall.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.javajober.core.component.FileImageService;
import com.javajober.core.message.SuccessMessage;
import com.javajober.core.util.ApiUtils;
import com.javajober.spaceWall.domain.FlagType;
import com.javajober.spaceWall.dto.request.DeleteTemporaryRequest;
import com.javajober.spaceWall.dto.request.SpaceWallRequest;
import com.javajober.spaceWall.dto.request.SpaceWallUpdateRequest;
import com.javajober.spaceWall.dto.response.SpaceWallResponse;
import com.javajober.spaceWall.dto.response.SpaceWallSaveResponse;
import com.javajober.spaceWall.dto.response.SpaceWallTemporaryResponse;
import com.javajober.spaceWall.service.SpaceWallFindService;
import com.javajober.spaceWall.service.SpaceWallService;
import com.javajober.spaceWall.service.SpaceWallTemporaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequestMapping("/api")
@RestController
public class SpaceWallController {

    private final SpaceWallService spaceWallService;
    private final SpaceWallFindService spaceWallFindService;
    private final SpaceWallTemporaryService spaceWallTemporaryService;
    private final FileImageService fileImageService;

    public SpaceWallController(final SpaceWallService spaceWallService, final SpaceWallFindService spaceWallFindService,
                               final SpaceWallTemporaryService spaceWallTemporaryService, final FileImageService fileImageService) {
        this.spaceWallService = spaceWallService;
        this.spaceWallFindService = spaceWallFindService;
        this.spaceWallTemporaryService = spaceWallTemporaryService;
        this.fileImageService = fileImageService;
    }

    @GetMapping("/wall-temporary/storage/{memberId}/{addSpaceId}")
    public ResponseEntity<ApiUtils.ApiResponse<SpaceWallTemporaryResponse>> checkSpaceWallTemporary(@PathVariable Long memberId, @PathVariable Long addSpaceId) {

        SpaceWallTemporaryResponse response = spaceWallTemporaryService.checkSpaceWallTemporary(memberId, addSpaceId);

        return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.SPACE_WALL_TEMPORARY_READ_SUCCESS, response));
    }

    @PostMapping(path = "/wall", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiUtils.ApiResponse<SpaceWallSaveResponse>> save(@RequestPart(value = "data") final SpaceWallRequest spaceWallRequest,
                                                                            @RequestPart(value = "fileName") List<MultipartFile> files,
                                                                            @RequestPart(value = "backgroundImgURL", required = false) MultipartFile backgroundImgURL,
                                                                            @RequestPart(value = "wallInfoImgURL", required = false) MultipartFile wallInfoImgURL,
                                                                            @RequestPart(value = "styleImgURL", required = false) MultipartFile styleImgURL) {

        fileImageService.validatePdfFile(files);
        SpaceWallSaveResponse response = spaceWallService.save(spaceWallRequest, FlagType.SAVED, files, backgroundImgURL, wallInfoImgURL, styleImgURL);

        return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.SPACE_WALL_SAVE_SUCCESS, response));
    }

    @PostMapping(path = "/wall-temporary", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiUtils.ApiResponse<SpaceWallSaveResponse>> savePending (@RequestPart(value = "data") final SpaceWallRequest spaceWallRequest,
                                                                                    @RequestPart(value = "fileName", required = false) List<MultipartFile> files,
                                                                                    @RequestPart(value = "backgroundImgURL", required = false) MultipartFile backgroundImgURL,
                                                                                    @RequestPart(value = "wallInfoImgURL", required = false) MultipartFile wallInfoImgURL,
                                                                                    @RequestPart(value = "styleImgURL", required = false) MultipartFile styleImgURL) {
        fileImageService.validatePdfFile(files);
        SpaceWallSaveResponse response = spaceWallService.save(spaceWallRequest, FlagType.PENDING, files, backgroundImgURL, wallInfoImgURL, styleImgURL);

        return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.SPACE_WALL_TEMPORARY_SAVE_SUCCESS, response));
    }

    @PutMapping("/wall")
    public ResponseEntity<?> update(@RequestBody final SpaceWallUpdateRequest spaceWallUpdateRequest){

        spaceWallService.update(spaceWallUpdateRequest, FlagType.SAVED);

        return  ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.CREATE_SUCCESS, null));
    }

    @GetMapping("/wall/{memberId}/{addSpaceId}/{spaceWallId}")
    public ResponseEntity<ApiUtils.ApiResponse<SpaceWallResponse>> findSaved (
            @PathVariable Long memberId, @PathVariable Long addSpaceId, @PathVariable Long spaceWallId) throws JsonProcessingException {

        SpaceWallResponse data = spaceWallFindService.find(memberId, addSpaceId, spaceWallId, FlagType.SAVED);
        return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.SPACE_WALL_READ_SUCCESS, data));
    }

    @GetMapping("/wall-temporary/{memberId}/{addSpaceId}/{spaceWallId}")
    public ResponseEntity<ApiUtils.ApiResponse<SpaceWallResponse>> findPending(
            @PathVariable Long memberId, @PathVariable Long addSpaceId, @PathVariable Long spaceWallId) throws JsonProcessingException {

        SpaceWallResponse data = spaceWallFindService.find(memberId, addSpaceId, spaceWallId, FlagType.PENDING);
        return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.SPACE_WALL_TEMPORARY_READ_SUCCESS, data));
    }

    @PutMapping("/wall-temporary")
    public ResponseEntity<ApiUtils.ApiResponse> deleteTemporary(@RequestBody final DeleteTemporaryRequest deleteTemporaryRequest) {

        Long memberId = deleteTemporaryRequest.getMemberId();
        Long addSpaceId = deleteTemporaryRequest.getAddSpaceId();

        spaceWallTemporaryService.deleteTemporary(memberId, addSpaceId);

        return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.SPACE_WALL_TEMPORARY_DELETE_SUCCESS, null));
    }
}
