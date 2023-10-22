package com.javajober.spaceWall.strategy.impl;

import org.springframework.stereotype.Component;

import com.javajober.blocks.wallInfoBlock.domain.WallInfoBlock;
import com.javajober.blocks.wallInfoBlock.dto.request.WallInfoBlockStringSaveRequest;
import com.javajober.blocks.wallInfoBlock.repository.WallInfoBlockRepository;
import com.javajober.spaceWall.dto.request.DataStringSaveRequest;
import com.javajober.spaceWall.strategy.BlockStrategyName;
import com.javajober.spaceWall.strategy.FixBlockStrategy;

@Component
public class WallInfoBlockStrategy implements FixBlockStrategy {

	private final WallInfoBlockRepository wallInfoBlockRepository;

	public WallInfoBlockStrategy(WallInfoBlockRepository wallInfoBlockRepository) {
		this.wallInfoBlockRepository = wallInfoBlockRepository;
	}

	@Override
	public Long saveBlocks(DataStringSaveRequest data) {
		WallInfoBlockStringSaveRequest request = data.getWallInfoBlock();
		return saveWallInfoBlock(request);
	}

	@Override
	public String getStrategyName() {
		return BlockStrategyName.WallInfoBlockStrategy.name();
	}

	private Long saveWallInfoBlock(WallInfoBlockStringSaveRequest request) {
		WallInfoBlock wallInfoBlock = WallInfoBlockStringSaveRequest.toEntity(request);
		return wallInfoBlockRepository.save(wallInfoBlock).getId();
	}
}
