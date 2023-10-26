package com.javajober.spaceWall.controller;

import com.javajober.core.exception.ApiStatus;
import com.javajober.core.security.JwtTokenizer;
import com.javajober.core.util.file.FileImageService;
import com.javajober.core.util.response.ApiResponse;
import com.javajober.spaceWall.domain.FlagType;
import com.javajober.spaceWall.filedto.SpaceWallSaveRequest;
import com.javajober.spaceWall.dto.response.SpaceWallSaveResponse;
import com.javajober.spaceWall.filedto.SpaceWallUpdateRequest;
import com.javajober.spaceWall.service.FileUploadService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api")
@Controller
public class FileUploadController {

    private final FileImageService fileImageService;
    private final FileUploadService fileUploadService;
    private final JwtTokenizer jwtTokenizer;

    public FileUploadController(final FileImageService fileImageService, final FileUploadService fileUploadService, final JwtTokenizer jwtTokenizer) {
        this.fileImageService = fileImageService;
        this.fileUploadService = fileUploadService;
        this.jwtTokenizer = jwtTokenizer;
    }

    @PostMapping(path = "/wall/file", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse.Response<SpaceWallSaveResponse>> save(@RequestPart(value = "data") final SpaceWallSaveRequest spaceWallSaveRequest,
                                                                            @RequestPart(value = "fileName") List<MultipartFile> files,
                                                                            @RequestPart(value = "backgroundImgURL", required = false) MultipartFile backgroundImgURL,
                                                                            @RequestPart(value = "wallInfoImgURL", required = false) MultipartFile wallInfoImgURL,
                                                                            @RequestPart(value = "styleImgURL", required = false) MultipartFile styleImgURL,
                                                                            @RequestHeader("Authorization") String token) {

        Long memberId = jwtTokenizer.getUserIdFromToken(token);
        fileImageService.validatePdfFile(files);
        fileImageService.checkImageFileSize(backgroundImgURL, wallInfoImgURL, styleImgURL);
        SpaceWallSaveResponse data = fileUploadService.save(memberId, spaceWallSaveRequest, FlagType.SAVED, files, backgroundImgURL, wallInfoImgURL, styleImgURL);

        return ApiResponse.response(ApiStatus.OK, "공유페이지 저장이 완료되었습니다.", data);
    }

    @PostMapping(path = "wall-temporary/file", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse.Response<SpaceWallSaveResponse>> savePending(@RequestPart(value = "data") final SpaceWallSaveRequest spaceWallSaveRequest,
        @RequestPart(value = "fileName") List<MultipartFile> files,
        @RequestPart(value = "backgroundImgURL", required = false) MultipartFile backgroundImgURL,
        @RequestPart(value = "wallInfoImgURL", required = false) MultipartFile wallInfoImgURL,
        @RequestPart(value = "styleImgURL", required = false) MultipartFile styleImgURL,
        @RequestHeader("Authorization") String token) {

        Long memberId = jwtTokenizer.getUserIdFromToken(token);
        fileImageService.validatePdfFile(files);
        fileImageService.checkImageFileSize(backgroundImgURL, wallInfoImgURL, styleImgURL);
        SpaceWallSaveResponse data = fileUploadService.save(memberId, spaceWallSaveRequest, FlagType.PENDING, files, backgroundImgURL, wallInfoImgURL, styleImgURL);

        return ApiResponse.response(ApiStatus.OK, "공유페이지 임시저장이 완료되었습니다.", data);
    }

    @PutMapping(path = "/wall/file", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse.Response<SpaceWallSaveResponse>> update(@RequestPart(value = "data") final SpaceWallUpdateRequest spaceWallRequest,
                                                                              @RequestPart(value = "fileName") List<MultipartFile> files,
                                                                              @RequestPart(value = "backgroundImgURL", required = false) MultipartFile backgroundImgURL,
                                                                              @RequestPart(value = "wallInfoImgURL", required = false) MultipartFile wallInfoImgURL,
                                                                              @RequestPart(value = "styleImgURL", required = false) MultipartFile styleImgURL,
                                                                              @RequestHeader("Authorization") String token){

        Long memberId = jwtTokenizer.getUserIdFromToken(token);
        fileImageService.validatePdfFile(files);
        fileImageService.checkImageFileSize(backgroundImgURL, wallInfoImgURL, styleImgURL);
        SpaceWallSaveResponse data = fileUploadService.update(memberId, spaceWallRequest, FlagType.SAVED, files, backgroundImgURL, wallInfoImgURL, styleImgURL);

        return ApiResponse.response(ApiStatus.OK, "공유페이지 수정이 완료되었습니다.", data);
    }

    @PutMapping(path = "wall-temporary/file", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse.Response<SpaceWallSaveResponse>> updatePending(@RequestPart(value = "data") final SpaceWallUpdateRequest spaceWallRequest,
        @RequestPart(value = "fileName") List<MultipartFile> files,
        @RequestPart(value = "backgroundImgURL", required = false) MultipartFile backgroundImgURL,
        @RequestPart(value = "wallInfoImgURL", required = false) MultipartFile wallInfoImgURL,
        @RequestPart(value = "styleImgURL", required = false) MultipartFile styleImgURL,
        @RequestHeader("Authorization") String token){

        Long memberId = jwtTokenizer.getUserIdFromToken(token);
        fileImageService.validatePdfFile(files);
        fileImageService.checkImageFileSize(backgroundImgURL, wallInfoImgURL, styleImgURL);
        SpaceWallSaveResponse data = fileUploadService.update(memberId, spaceWallRequest, FlagType.PENDING, files, backgroundImgURL, wallInfoImgURL, styleImgURL);

        return ApiResponse.response(ApiStatus.OK, "공유페이지 임시저장이 완료되었습니다.", data);
    }
}
