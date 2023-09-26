package com.javajober.fcm.controller;

import com.javajober.core.message.SuccessMessage;
import com.javajober.core.util.ApiUtils;
import com.javajober.fcm.dto.SubscriptionRequest;
import com.javajober.fcm.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wall")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/subscription")
    public ResponseEntity<ApiUtils.ApiResponse<Object>> subscribe(@RequestBody SubscriptionRequest request) {
        subscriptionService.subscribe(request);
        return ResponseEntity.ok(ApiUtils.success(HttpStatus.CREATED, SuccessMessage.CREATE_SUCCESS, null));
    }
}