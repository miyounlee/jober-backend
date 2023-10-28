package com.javajober.spaceWall.strategy.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.javajober.blocks.styleSetting.backgroundSetting.dto.request.BackgroundStringUpdateRequest;
import com.javajober.blocks.styleSetting.backgroundSetting.dto.response.BackgroundSettingResponse;
import com.javajober.blocks.styleSetting.backgroundSetting.filedto.BackgroundSettingSaveRequest;
import com.javajober.blocks.styleSetting.backgroundSetting.filedto.BackgroundSettingUpdateRequest;
import com.javajober.blocks.styleSetting.blockSetting.dto.request.BlockSettingUpdateRequest;
import com.javajober.blocks.styleSetting.blockSetting.dto.response.BlockSettingResponse;
import com.javajober.blocks.styleSetting.dto.request.StyleSettingStringUpdateRequest;
import com.javajober.blocks.styleSetting.dto.response.StyleSettingResponse;
import com.javajober.blocks.styleSetting.filedto.StyleSettingSaveRequest;
import com.javajober.blocks.styleSetting.filedto.StyleSettingUpdateRequest;
import com.javajober.blocks.styleSetting.themeSetting.dto.request.ThemeSettingUpdateRequest;
import com.javajober.blocks.styleSetting.themeSetting.dto.response.ThemeSettingResponse;
import com.javajober.core.util.file.FileImageService;
import com.javajober.core.util.response.CommonResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.javajober.blocks.styleSetting.backgroundSetting.domain.BackgroundSetting;
import com.javajober.blocks.styleSetting.backgroundSetting.dto.request.BackgroundSettingStringSaveRequest;
import com.javajober.blocks.styleSetting.backgroundSetting.repository.BackgroundSettingRepository;
import com.javajober.blocks.styleSetting.blockSetting.domain.BlockSetting;
import com.javajober.blocks.styleSetting.blockSetting.dto.request.BlockSettingSaveRequest;
import com.javajober.blocks.styleSetting.blockSetting.repository.BlockSettingRepository;
import com.javajober.blocks.styleSetting.domain.StyleSetting;
import com.javajober.blocks.styleSetting.dto.request.StyleSettingStringSaveRequest;
import com.javajober.blocks.styleSetting.repository.StyleSettingRepository;
import com.javajober.blocks.styleSetting.themeSetting.domain.ThemeSetting;
import com.javajober.blocks.styleSetting.themeSetting.dto.request.ThemeSettingSaveRequest;
import com.javajober.blocks.styleSetting.themeSetting.repository.ThemeSettingRepository;
import com.javajober.spaceWall.domain.BlockType;
import com.javajober.spaceWall.dto.request.DataStringSaveRequest;
import com.javajober.spaceWall.dto.request.DataStringUpdateRequest;
import com.javajober.spaceWall.filedto.DataSaveRequest;
import com.javajober.spaceWall.filedto.DataUpdateRequest;
import com.javajober.spaceWall.strategy.BlockJsonProcessor;
import com.javajober.spaceWall.strategy.BlockStrategyName;
import com.javajober.spaceWall.strategy.FixBlockStrategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class StyleSettingStrategy implements FixBlockStrategy {

	private final BlockJsonProcessor blockJsonProcessor;
	private final StyleSettingRepository styleSettingRepository;
	private final BackgroundSettingRepository backgroundSettingRepository;
	private final BlockSettingRepository blockSettingRepository;
	private final ThemeSettingRepository themeSettingRepository;
	private final FileImageService fileImageService;
	private final AtomicReference<String> uploadedStyleImageURL = new AtomicReference<>();

	public StyleSettingStrategy(final BlockJsonProcessor blockJsonProcessor, final StyleSettingRepository styleSettingRepository,
								final BackgroundSettingRepository backgroundSettingRepository, final BlockSettingRepository blockSettingRepository,
								final ThemeSettingRepository themeSettingRepository, FileImageService fileImageService) {
		this.blockJsonProcessor = blockJsonProcessor;
		this.styleSettingRepository = styleSettingRepository;
		this.backgroundSettingRepository = backgroundSettingRepository;
		this.blockSettingRepository = blockSettingRepository;
		this.themeSettingRepository = themeSettingRepository;
		this.fileImageService = fileImageService;
	}

	@Override
	public void saveBlocks(final DataSaveRequest data, final ArrayNode blockInfoArray, final Long position) {
		StyleSettingSaveRequest request = data.getStyleSetting();

		Long styleSettingId = saveStyleSetting(request);
		blockJsonProcessor.addBlockInfoToArray(blockInfoArray, position, BlockType.STYLE_SETTING, styleSettingId, "");
	}

	@Override
	public void uploadSingleFile(final MultipartFile styleImgURL) {
		uploadedStyleImageURL.set(fileImageService.uploadFile(styleImgURL));
	}

	private Long saveStyleSetting(final StyleSettingSaveRequest request) {

		BackgroundSettingSaveRequest backgroundRequest = request.getBackgroundSetting();
		BackgroundSetting backgroundSetting = backgroundSettingRepository.save(BackgroundSettingSaveRequest.toEntity(backgroundRequest, uploadedStyleImageURL.get()));

		BlockSettingSaveRequest blockSettingRequest = request.getBlockSetting();
		BlockSetting blockSetting = blockSettingRepository.save(BlockSettingSaveRequest.toEntity(blockSettingRequest));

		ThemeSettingSaveRequest themeSettingRequest = request.getThemeSetting();
		ThemeSetting themeSetting = themeSettingRepository.save(ThemeSettingSaveRequest.toEntity(themeSettingRequest));

		StyleSetting styleSetting = request.toEntity(backgroundSetting, blockSetting, themeSetting);

		return styleSettingRepository.save(styleSetting).getId();
	}

	@Override
	public void saveStringBlocks(final DataStringSaveRequest data, final ArrayNode blockInfoArray, final Long position) {
		StyleSettingStringSaveRequest request = data.getStyleSetting();

		Long styleSettingId = saveStringStyleSetting(request);
		blockJsonProcessor.addBlockInfoToArray(blockInfoArray, position, BlockType.STYLE_SETTING, styleSettingId, "");
	}

	private Long saveStringStyleSetting(final StyleSettingStringSaveRequest request) {

		BackgroundSettingStringSaveRequest backgroundRequest = request.getBackgroundSetting();
		BackgroundSetting backgroundSetting = backgroundSettingRepository.save(BackgroundSettingStringSaveRequest.toEntity(backgroundRequest));

		BlockSettingSaveRequest blockSettingRequest = request.getBlockSetting();
		BlockSetting blockSetting = blockSettingRepository.save(BlockSettingSaveRequest.toEntity(blockSettingRequest));

		ThemeSettingSaveRequest themeSettingRequest = request.getThemeSetting();
		ThemeSetting themeSetting = themeSettingRepository.save(ThemeSettingSaveRequest.toEntity(themeSettingRequest));

		StyleSetting styleSetting = request.toEntity(backgroundSetting, blockSetting, themeSetting);

		return styleSettingRepository.save(styleSetting).getId();
	}

	@Override
	public CommonResponse createFixBlockDTO(final List<JsonNode> fixBlocks) {
		Long blockId = fixBlocks.get(0).path("block_id").asLong();
		StyleSetting styleSetting = styleSettingRepository.findStyleBlock(blockId);

		BackgroundSettingResponse backgroundSettingResponse = BackgroundSettingResponse.from(styleSetting.getBackgroundSetting());
		BlockSettingResponse blockSettingResponse = BlockSettingResponse.from(styleSetting.getBlockSetting());
		ThemeSettingResponse themeSettingResponse = ThemeSettingResponse.from(styleSetting.getThemeSetting());

		return new StyleSettingResponse(blockId, backgroundSettingResponse, blockSettingResponse, themeSettingResponse);
	}

	@Override
	public void updateStringBlocks(final DataStringUpdateRequest data, final ArrayNode blockInfoArray, final Long position) {
		StyleSettingStringUpdateRequest request = data.getStyleSetting();

		Long styleSettingId = updateStringStyleSetting(request);

		blockJsonProcessor.addBlockInfoToArray(blockInfoArray, position, BlockType.STYLE_SETTING, styleSettingId, "");
	}

	private Long updateStringStyleSetting(final StyleSettingStringUpdateRequest request){

		BackgroundStringUpdateRequest backgroundRequest = request.getBackgroundSetting();
		BackgroundSetting backgroundSetting = backgroundSettingRepository.getById(backgroundRequest.getBackgroundSettingBlockId());
		backgroundSetting.update(backgroundRequest);
		backgroundSettingRepository.save(backgroundSetting);

		BlockSettingUpdateRequest blockSettingRequest = request.getBlockSetting();
		BlockSetting blockSetting = blockSettingRepository.getById(blockSettingRequest.getBlockSettingBlockId());
		blockSetting.update(blockSettingRequest);
		blockSettingRepository.save(blockSetting);

		ThemeSettingUpdateRequest themeSettingRequest = request.getThemeSetting();
		ThemeSetting themeSetting = themeSettingRepository.getById(themeSettingRequest.getThemeSettingBlockId());
		themeSetting.update(themeSettingRequest);
		themeSettingRepository.save(themeSetting);

		StyleSetting styleSetting = styleSettingRepository.findStyleBlock(request.getStyleSettingBlockId());
		styleSetting.update(backgroundSetting, blockSetting, themeSetting);

		return styleSettingRepository.save(styleSetting).getId();
	}

	@Override
	public void updateBlocks(DataUpdateRequest data, ArrayNode blockInfoArray, Long position) {
		StyleSettingUpdateRequest request = data.getStyleSetting();

		Long styleSettingId = updateStyleSetting(request);

		blockJsonProcessor.addBlockInfoToArray(blockInfoArray, position, BlockType.STYLE_SETTING, styleSettingId, "");
	}

	private Long updateStyleSetting(final StyleSettingUpdateRequest request){

		BackgroundSettingUpdateRequest backgroundRequest = request.getBackgroundSetting();
		BackgroundSetting backgroundSetting = backgroundSettingRepository.getById(backgroundRequest.getBackgroundSettingBlockId());
		backgroundSetting.update(backgroundRequest, uploadedStyleImageURL.get());
		backgroundSettingRepository.save(backgroundSetting);

		BlockSettingUpdateRequest blockSettingRequest = request.getBlockSetting();
		BlockSetting blockSetting = blockSettingRepository.getById(blockSettingRequest.getBlockSettingBlockId());
		blockSetting.update(blockSettingRequest);
		blockSettingRepository.save(blockSetting);

		ThemeSettingUpdateRequest themeSettingRequest = request.getThemeSetting();
		ThemeSetting themeSetting = themeSettingRepository.getById(themeSettingRequest.getThemeSettingBlockId());
		themeSetting.update(themeSettingRequest);
		themeSettingRepository.save(themeSetting);

		StyleSetting styleSetting = styleSettingRepository.findStyleBlock(request.getStyleSettingBlockId());
		styleSetting.update(backgroundSetting, blockSetting, themeSetting);

		return styleSettingRepository.save(styleSetting).getId();
	}

	@Override
	public String getStrategyName() {
		return BlockStrategyName.StyleSettingStrategy.name();
	}
}
