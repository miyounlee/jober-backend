package com.javajober.home.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javajober.core.security.JwtTokenizer;
import com.javajober.core.util.response.ApiResponse;
import com.javajober.core.exception.ApiStatus;

import com.javajober.home.dto.response.HomeResponse;
import com.javajober.home.service.HomeService;

@RestController
@RequestMapping("/api/home")
public class HomeController {

	private final HomeService homeService;
	private final JwtTokenizer jwtTokenizer;

	private HomeController (final HomeService homeService, final JwtTokenizer jwtTokenizer) {
		this.homeService = homeService;
		this.jwtTokenizer = jwtTokenizer;
	}

	@GetMapping
	public ResponseEntity<ApiResponse.Response<HomeResponse>> find(@RequestHeader("Authorization") final String token) {

		Long memberId = jwtTokenizer.getUserIdFromToken(token);
		HomeResponse data = homeService.find(memberId);

		return ApiResponse.response(ApiStatus.OK, "홈 조회를 성공했습니다.", data);
	}
}