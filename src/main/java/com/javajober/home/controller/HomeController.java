package com.javajober.home.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javajober.core.message.SuccessMessage;
import com.javajober.core.util.ApiUtils;
import com.javajober.home.dto.response.HomeResponse;
import com.javajober.home.service.HomeService;

@RestController
@RequestMapping("/api/home")
public class HomeController {

	private final HomeService homeService;

	private HomeController (final HomeService homeService) {
		this.homeService = homeService;
	}

	@GetMapping("/{memberId}")
	public ResponseEntity<ApiUtils.ApiResponse<HomeResponse>> getHome(@PathVariable Long memberId) {
		HomeResponse responses = homeService.getHomes(memberId);
		return ResponseEntity.ok(ApiUtils.success(HttpStatus.OK, SuccessMessage.HOME_SUCCESS, responses));
	}
}