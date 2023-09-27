package com.javajober.template.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javajober.core.message.SuccessMessage;
import com.javajober.core.util.ApiUtils;
import com.javajober.template.dto.TemplateBlockRequest;
import com.javajober.template.dto.TemplateBlockRequests;
import com.javajober.template.dto.TemplateBlockResponse;
import com.javajober.template.service.TemplateBlockService;

@RestController
public class TemplateBlockController {

	private final TemplateBlockService templateBlockService;

	public TemplateBlockController(TemplateBlockService templateBlockService) {
		this.templateBlockService = templateBlockService;
	}

	@PostMapping("/templateBlock")
	public ResponseEntity<ApiUtils.ApiResponse> createTemplateBlock(@RequestBody TemplateBlockRequests<TemplateBlockRequest> templateBlockRequests) {

		templateBlockService.save(templateBlockRequests);

		return ResponseEntity.ok(ApiUtils.success(HttpStatus.CREATED, SuccessMessage.TEMPLATE_BLOCK_SAVE_SUCCESS, null));
	}

	@GetMapping("/templateBlock")
	public ResponseEntity<ApiUtils.ApiResponse<TemplateBlockResponse>> readTemplateBlock(@RequestParam Long templateBlockId) {

		TemplateBlockResponse response = templateBlockService.getTemplateBlock(templateBlockId);

		return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.TEMPLATE_BLOCK_READ_SUCCESS, response));
	}

	@PutMapping("/templateBlock")
	public ResponseEntity<ApiUtils.ApiResponse> deleteTemplateBlock (@RequestParam Long templateBlockId) {

		templateBlockService.deleteTemplateBlock(templateBlockId);

		return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.TEMPLATE_BLOCK_DELETE_SUCCESS, null));
	}
}