package com.javajober.template.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javajober.core.message.SuccessMessage;
import com.javajober.core.util.ApiUtils;
import com.javajober.spaceWall.domain.SpaceWallCategoryType;
import com.javajober.template.dto.MemberAuthResponse;
import com.javajober.entity.SpaceType;
import com.javajober.template.dto.TemplateResponse;
import com.javajober.template.service.TemplateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wall/templates")
public class TemplateController {
	private final TemplateService templateService;

	@GetMapping("/auth")
	public ResponseEntity<ApiUtils.ApiResponse<MemberAuthResponse>> getTemplateAuthList(@RequestParam SpaceType spaceType, @RequestParam Long memberId, @RequestParam Long templateBlockId) {
		MemberAuthResponse response = templateService.getTemplateAuthList(spaceType, memberId, templateBlockId);
		return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.TEMPLATE_AUTH_SUCCESS, response));
	}

	@GetMapping
	public ResponseEntity<ApiUtils.ApiResponse<TemplateResponse>> getTemplateRecommend(@RequestParam SpaceWallCategoryType category){
		TemplateResponse response = templateService.getTemplateRecommend(category);
		return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.TEMPLATE_RECOMMEND_SUCCESS, response));
	}

	@GetMapping("/lists")
	public ResponseEntity<ApiUtils.ApiResponse<TemplateResponse>> getTemplateCategoryList(@RequestParam SpaceWallCategoryType category){
		TemplateResponse response = templateService.getTemplateRecommend(category);
		return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.TEMPLATE_CATEGORY_SUCCESS, response));
	}

	@GetMapping(params = "search")
	public ResponseEntity<ApiUtils.ApiResponse<TemplateResponse>> getSearchTemplatesByTitle(@RequestParam String search){
		TemplateResponse response = templateService.getSearchTemplatesByTitle(search);
		return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.TEMPLATE_SEARCH_SUCCESS, response));
	}
}
