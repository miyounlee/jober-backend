package com.javajober.styleSetting.dto.response;

import com.javajober.backgroundSetting.dto.response.BackgroundSettingResponse;
import com.javajober.blockSetting.dto.response.BlockSettingResponse;
import com.javajober.themeSetting.dto.response.ThemeSettingResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StyleSettingResponse {

    private BackgroundSettingResponse backgroundSetting;
    private BlockSettingResponse blockSetting;
    private ThemeSettingResponse themeSetting;

    public StyleSettingResponse() {}

    @Builder
    public StyleSettingResponse(final BackgroundSettingResponse backgroundSetting, final BlockSettingResponse blockSetting, final ThemeSettingResponse themeSetting) {
        this.backgroundSetting = backgroundSetting;
        this.blockSetting = blockSetting;
        this.themeSetting = themeSetting;
    }

    public static StyleSettingResponse from(final BackgroundSettingResponse backgroundSettingResponse,
                                            final BlockSettingResponse blockSettingResponse,
                                            final ThemeSettingResponse themeSettingResponse) {
        return StyleSettingResponse.builder()
                .backgroundSetting(backgroundSettingResponse)
                .blockSetting(blockSettingResponse)
                .themeSetting(themeSettingResponse)
                .build();
    }
}
