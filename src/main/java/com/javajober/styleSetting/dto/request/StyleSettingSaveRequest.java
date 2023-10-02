package com.javajober.styleSetting.dto.request;
 ;
 import com.javajober.backgroundSetting.dto.request.BackgroundSettingSaveRequest;
 import com.javajober.blockSetting.dto.request.BlockSettingSaveRequest;
 import com.javajober.styleSetting.domain.StyleSetting;
 import com.javajober.themeSetting.dto.request.ThemeSettingSaveRequest;

 import lombok.Getter;

@Getter
public class StyleSettingSaveRequest {

	private BackgroundSettingSaveRequest backgroundSetting;
	private BlockSettingSaveRequest blockSetting;
	private ThemeSettingSaveRequest themeSetting;

	public StyleSettingSaveRequest(){

	}


	public StyleSetting toEntity() {
		return StyleSetting.builder()
			.backgroundSetting(backgroundSetting.toEntity())
			.blockSetting(blockSetting.toEntity())
			.themeSetting(themeSetting.toEntity())
			.build();
	}
}
