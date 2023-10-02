package com.javajober.backgroundSetting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javajober.core.config.FileDirectoryConfig;
import com.javajober.backgroundSetting.domain.BackgroundSetting;
import com.javajober.backgroundSetting.dto.request.BackgroundSettingSaveRequest;
import com.javajober.backgroundSetting.repository.BackgroundSettingRepository;

@Service
public class BackgroundSettingService {

	private final BackgroundSettingRepository backgroundSettingRepository;

	private final FileDirectoryConfig fileDirectoryConfig;

	public BackgroundSettingService(BackgroundSettingRepository backgroundSettingRepository,
		FileDirectoryConfig fileDirectoryConfig) {
		this.backgroundSettingRepository = backgroundSettingRepository;
		this.fileDirectoryConfig = fileDirectoryConfig;
	}

	@Transactional
	public BackgroundSetting save(final BackgroundSettingSaveRequest saveRequest){

		//String styleImg = uploadImg(file);

		BackgroundSetting backgroundSetting = saveRequest.toEntity();

		return backgroundSettingRepository.save(backgroundSetting);
	}
}
