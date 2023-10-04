package com.javajober.styleSetting.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javajober.core.message.SuccessMessage;
import com.javajober.core.util.ApiUtils;
import com.javajober.styleSetting.dto.request.StyleSettingSaveRequest;
import com.javajober.styleSetting.service.StyleSettingService;

@RequestMapping("/api/setting/style")
@RestController
public class StyleSettingController {

	private final StyleSettingService styleSettingService;

	public StyleSettingController(StyleSettingService styleSettingService) {
		this.styleSettingService = styleSettingService;
	}

//	@PostMapping
//	public ResponseEntity<ApiUtils.ApiResponse> save(@RequestBody StyleSettingSaveRequest styleSettingSaveRequest, String styleImgURL) {
//		styleSettingService.save(styleSettingSaveRequest, styleImgURL);
//		return ResponseEntity.ok(ApiUtils.success(HttpStatus.CREATED, SuccessMessage.CREATE_SUCCESS, null));
//	}
}
