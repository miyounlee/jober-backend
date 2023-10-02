package com.javajober.backgroundSetting.dto.request;

import com.javajober.backgroundSetting.domain.BackgroundSetting;

import lombok.Getter;

@Getter
public class BackgroundSettingSaveRequest {
	private String solidColor;
	private boolean gradation;

	public BackgroundSettingSaveRequest(){

	}
	public BackgroundSetting toEntity() {
		return BackgroundSetting.builder()
			.solidColor(this.getSolidColor())
			.gradation(this.isGradation())
			.build();
	}
}
