package com.javajober.fcm.controller;

import com.javajober.fcm.dto.MemberFcmTokenRequest;
import com.javajober.fcm.service.MemberFcmTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javajober.core.message.SuccessMessage;
import com.javajober.core.util.ApiUtils;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fcm-token")
public class MemberFcmTokenController {

    private final MemberFcmTokenService memberFcmTokenService;

    public MemberFcmTokenController(final MemberFcmTokenService memberFcmTokenService) {
        this.memberFcmTokenService = memberFcmTokenService;
    }

    @PostMapping
    public ResponseEntity<ApiUtils.ApiResponse<Object>> saveFcmToken(@RequestBody MemberFcmTokenRequest request) {
        memberFcmTokenService.saveFcmToken(request);
        return ResponseEntity.ok(ApiUtils.success(HttpStatus.CREATED, SuccessMessage.CREATE_SUCCESS, null));
    }
}