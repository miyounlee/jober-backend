package com.javajober.styleSetting.repository;

import org.springframework.data.repository.Repository;

import com.javajober.styleSetting.domain.StyleSetting;

public interface StyleSettingRepository extends Repository<StyleSetting, Long> {

	StyleSetting save(StyleSetting styleSetting);
}
